import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class FlasherTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Flasher"
  def testFn[T <: FlasherBase](f: T) = {
    f.io.start.poke(0.U)
    f.io.light.expect(0.U)
    f.clock.step()
    f.io.light.expect(0.U)
    f.clock.step()
    f.io.light.expect(0.U)
    f.io.start.poke(1.U)
    f.clock.step()
    f.io.start.poke(0.U)
    f.io.light.expect(1.U)
    f.clock.step(5)
    f.io.light.expect(1.U)
    f.clock.step()
    for (_ <- 0 until 2) {
      f.io.light.expect(0.U)
      f.clock.step(3)
      f.io.light.expect(0.U)
      f.clock.step()
      f.io.light.expect(1.U)
      f.clock.step(5)
      f.io.light.expect(1.U)
      f.clock.step()
      f.io.light.expect(0.U)
    }
    f.clock.step()
    f.io.light.expect(0.U)
    f.clock.step(5)
    f.io.light.expect(0.U)
  }

  it should "pass" in {
    test(new Flasher).withAnnotations(Seq(WriteVcdAnnotation))  { dut => testFn(dut) }
    test(new Flasher2).withAnnotations(Seq(WriteVcdAnnotation)) { dut => testFn(dut) }
  }
}
