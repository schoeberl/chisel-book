import chisel3._
import chisel3.util._

import java.lang.Math.{floor, log10, pow}

// Only one will be ready, as we cannot take two values
// This would need a shadow register, a reasonable optimisation
// Without optimisation one channel can only take one data every 2 clock cycles

// the following should error (in the Module), as this is the wrong direction
// out.ready := a.ready


object TreeReduce {
  implicit class SeqToTreeReducible[T](xs: Seq[T]) {
    def treeReduce(op: (T,T) => T): T = {
      xs match {
        case Seq(x) => x
        case s => s.grouped(2).map {
          case Seq(x) => x
          case Seq(x,y) => op(x,y)
        }.toSeq.treeReduce(op)
      }
    }
  }
}
import TreeReduce.SeqToTreeReducible

class ArbiterTreeExperiments[T <: Data: Manifest](n: Int, private val gen: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Vec(n, new DecoupledIO(gen)))
    val out = new DecoupledIO(gen)
  })

  def myUnfairTree[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val l = s.length
    require(l > 0, "Cannot apply reduction on a Seq of size 0")
    l match {
      case 1 => s(0)
      case 2 => println("in case 2"); op(s(0), s(1))
      case _ => {
        // Maybe with Array[Any] we can avoid the Manifest thing
        val ns = new Array[T]((l+1)/2)
        for (i <- 0 until l/2) {
          ns(i) = op(s(i * 2), s(i * 2 + 1))
        }
        if (l % 2 == 1) {
          ns(l / 2) = s(l - 1)
        }
        myUnfairTree(ns.toIndexedSeq, op)
      }
    }
  }

  def myTree[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val n = s.length
    require(n > 0, "Cannot apply reduction on a Seq of size 0")

    n match {
      case 1 => s(0)
      case 2 => op(s(0), s(1))
      case _ =>
        val m =  pow(2, floor(log10(n-1)/log10(2))).toInt // number of nodes in upper level
        val ns = new Array[T](m)
        val k = 2 * (n - m)
        val p = n - k
        for (i <- 0 until p) {
          ns(i) = s(i)
        }
        for (i <- 0 until k by 2) {
          ns(p + i/2) = op(s(p+i), s(p+i+1))
        }
        myTree(ns.toIndexedSeq, op)
    }
  }

  def myTreeFunctional[T](s: Seq[T], op: (T, T) => T): T = {

    val n = s.length
    require(n > 0, "Cannot apply reduction on a Seq of size 0")

    n match {
      case 1 => s(0)
      case 2 => op(s(0), s(1))
      case _ =>
        val m =  pow(2, floor(log10(n-1)/log10(2))).toInt // number of nodes in next level, will be a power of 2
        val p = 2 * m - n // number of nodes promoted

        val l =  s.take(p)
        val r = s.drop(p).grouped(2).map {
          case Seq(a, b) => op(a, b)
        }.toSeq

        myTreeFunctional(l ++ r, op)
    }
  }

  def arbitrateTwo(a: DecoupledIO[T], b: DecoupledIO[T]) = {

    object State extends ChiselEnum {
      val idleA, idleB, hasA, hasB = Value
    }
    import State._

    val regData = Reg(gen)
    val regState = RegInit(idleA)
    val out = Wire(new DecoupledIO(gen))

    a.ready := regState === idleA
    b.ready := regState === idleB
    out.valid := (regState === hasA || regState === hasB)

    switch(regState) {
      is (idleA) {
        when (a.valid) {
          regData := a.bits
          regState := hasA
        } otherwise {
          regState := idleB
        }
      }
      is (idleB) {
        when (b.valid) {
          regData := b.bits
          regState := hasB
        } otherwise {
          regState := idleA
        }
      }
      is (hasA) {
        when (out.ready) {
          regState := idleB
        }
      }
      is (hasB) {
        when (out.ready) {
          regState := idleA
        }
      }
    }

    out.bits := regData
    out
  }

  def add(a: DecoupledIO[T], b: DecoupledIO[T]) = {
    val out = Wire(new DecoupledIO(gen))
    out.bits := a.bits.asUInt() + b.bits.asUInt()
    a.ready := true.B
    b.ready := true.B
    out.valid := true.B
    out
  }
  // io.out <> io.in.reduceTree(foo)
  // io.out <> io.in.reduce(arbitrateTwo)
  // io.out <> io.in.treeReduce(add)
  io.out <> myTreeFunctional(io.in, arbitrateTwo)
}

object ArbiterTreeExperiments extends App {
  println(getVerilogString(new ArbiterTreeExperiments(7, UInt(8.W))))
}
