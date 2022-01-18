import chisel3._
import chisel3.util._

abstract class AbstractAdder(w: Int) extends Module {
  require(w > 0, "adders should have a positive width")
  val io = IO(new Bundle {
    val a    = Input(UInt(w.W))
    val b    = Input(UInt(w.W))
    val cin  = Input(Bool())
    val c    = Output(UInt(w.W))
    val cout = Output(Bool())
  })

  val isFacOf4 = w % 4 == 0
  val stages = if (isFacOf4) w / 4 else w / 4 + 1
  val aOp = (
    if (isFacOf4) io.a else 0.U((4-(w%4)).W) ## io.a
  ).asTypeOf(Vec(stages, UInt(4.W)))
  val bOp = (
    if (isFacOf4) io.b else 0.U((4-(w%4)).W) ## io.b
  ).asTypeOf(Vec(stages, UInt(4.W)))
}

class CarryRippleAdder(w: Int = 32) extends AbstractAdder(w) {
  val res = io.a +& io.b + io.cin.asUInt
  io.c    := res(w-1, 0)
  io.cout := res(w)
}

object CarryRippleAdder extends App {
  emitVerilog(new CarryRippleAdder, Array("--target-dir", "generated"))
}

class CarrySelectAdder(w: Int = 32) extends AbstractAdder(w) {
  val res   = Wire(Vec(stages, UInt(4.W)))
  var couts = Wire(Vec(stages+1, Bool()))
  couts(0)  := io.cin
  (0 until stages).foreach { i =>
    val (a, b, cin) = (aOp(i), bOp(i), couts(i))
    val (c0Sum, c1Sum) = ((a +& b), (a +& b + 1.U))
    res(i) := Mux(cin, c1Sum(3, 0), c0Sum(3, 0))
    couts(i+1) := Mux(cin, c1Sum(4), c0Sum(4))
  }

  io.c := res.asUInt()(w-1, 0)
  if (isFacOf4) io.cout := couts(stages) else io.cout := res.asUInt()(w)
}

object CarrySelectAdder extends App {
  emitVerilog(new CarrySelectAdder, Array("--target-dir", "generated"))
}

class CarrySkipAdder(w: Int = 32) extends AbstractAdder(w) {
  val res   = Wire(Vec(stages, UInt(4.W)))
  val couts = Wire(Vec(stages+1, Bool()))
  couts(0) := io.cin
  (0 until stages).foreach { i =>
    val (a, b, cin) = (aOp(i), bOp(i), couts(i))
    val sum = a +& b + cin.asUInt
    res(i) := sum(3, 0)
    couts(i+1) := Mux((a ^ b).andR(), cin, sum(4))
  }
  io.c := res.asUInt()(w-1, 0)
  if (isFacOf4) io.cout := couts(stages) else io.cout := res.asUInt()(w)
}

object CarrySkipAdder extends App {
  emitVerilog(new CarrySkipAdder, Array("--target-dir", "generated"))
}

class CarryLookaheadAdder(w: Int = 32) extends AbstractAdder(w) {
  val res   = Wire(Vec(stages, UInt(4.W)))
  val couts = Wire(Vec(stages+1, Bool()))
  couts(0) := io.cin
  (0 until stages).foreach { i =>
    val (a, b, cin) = (aOp(i), bOp(i), couts(i))
    val (p, g) = (a ^ b, a & b)
    val cs = Wire(Vec(5, Bool()))
    cs(0) := cin
    (0 until 4).foreach { i => cs(i+1) := g(i) | (p(i) & cs(i)) }
    res(i) := p ^ cs.asUInt()(3, 0)
    couts(i+1) := cs(4)
  }
  io.c := res.asUInt()(w-1, 0)
  if (isFacOf4) io.cout := couts(stages) else io.cout := res.asUInt()(w)
}

object CarryLookaheadAdder extends App {
  emitVerilog(new CarryLookaheadAdder, Array("--target-dir", "generated"))
}

abstract class PrefixAdder(w: Int) extends AbstractAdder(w) {
  require(isPow2(w)) // only working with powers of 2 width for now
  class GenAlivePair extends Bundle {
    val g = Bool()
    val a = Bool()
  }

  def fullDot(l: GenAlivePair, r: Bool) = l.g | (l.a & r)

  def emptyDot(l: GenAlivePair, r: GenAlivePair) = {
    val res = Wire(new GenAlivePair)
    res.g := l.g | (l.a & r.g)
    res.a := l.a & r.a
    res
  }

  val g  = io.a & io.b
  val a  = io.a | io.b
  val p  = io.a ^ io.b

  var temps  = (io.cin +: (0 until w).map { i =>
    val res = Wire(new GenAlivePair)
    res.g := g(i)
    res.a := a(i)
    res
  }).toArray
}

class BasicPrefixAdder(w: Int = 32) extends PrefixAdder(w) {
  // See page 83 Digital Arithmetic by Ercegovac and Lang
  (0 until w / 2 + 1).foreach { i =>
    var newTemps = temps
    if (i == 0) {
      (temps(1), temps(0)) match {
        case (p: GenAlivePair, b: Bool) => newTemps(1) = fullDot(p, b)
        case _ => // should never occur
      }
      (3 until w by 2).foreach { j =>
        (temps(j), temps(j-1)) match {
          case (l: GenAlivePair, r: GenAlivePair) => newTemps(j) = emptyDot(l, r)
          case _ => // should never occur
        }
      }
    } else {
      val start = i*2
      val r = temps(start-1)
      (temps(start), r) match {
        case (l1: GenAlivePair, r: Bool) => newTemps(start) = fullDot(l1, r)
        case _ => // should never occur
      }
      if (start+1 <= w) {
        val l2 = temps(start+1)
        (l2, r) match {
          case (l2: GenAlivePair, r: Bool) => newTemps(start+1) = fullDot(l2, r)
          case _ => // should never occur
        }
      }
    }
    temps = newTemps
  }
  val cs = VecInit(temps.toSeq).asUInt
  io.c := p ^ cs(w-1, 0)
  io.cout := cs(w)
}

object BasicPrefixAdder extends App {
  emitVerilog(new BasicPrefixAdder, Array("--target-dir", "generated"))
}

class MinFanoutPrefixAdder(w: Int = 32) extends PrefixAdder(w) {
  // See page 84 Digital Arithmetic by Ercegovac and Lang
  (0 until log2Up(w)).foreach { i => 
    val jump = 1 << i
    var newTemps = temps
    (jump until w+1).foreach { j =>
      (temps(j), temps(j-jump)) match {
        case (p1: GenAlivePair, p2: GenAlivePair) => newTemps(j) = emptyDot(p1, p2)
        case (p1: GenAlivePair, b : Bool) => newTemps(j) = fullDot(p1, b)
        case _ => // should never occur
      }
    }
    temps = newTemps
  }
  val cs = VecInit(temps.toSeq).asUInt
  io.c := p ^ cs(w-1, 0)
  io.cout := cs(w)
}

object MinFanoutPrefixAdder extends App {
  emitVerilog(new MinFanoutPrefixAdder, Array("--target-dir", "generated"))
}
