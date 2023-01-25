import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  "Memory" should "pass" in {
    test(new Memory) { dut =>
      // Fill the memory
      dut.io.wrEna.poke(true.B)
      for (i <- 0 to 10) {
        dut.io.wrAddr.poke(i.U)
        dut.io.wrData.poke((i*10).U)
        dut.clock.step()
      }
      dut.io.wrEna.poke(false.B)

      dut.io.rdAddr.poke(10.U)
      dut.clock.step()
      dut.io.rdData.expect(100.U)
      dut.io.rdAddr.poke(5.U)
      dut.io.rdData.expect(100.U)
      dut.clock.step()
      dut.io.rdData.expect(50.U)

      // Same address read and write
      dut.io.wrAddr.poke(20.U)
      dut.io.wrData.poke(123.U)
      dut.io.wrEna.poke(true.B)
      dut.io.rdAddr.poke(20.U)
      dut.clock.step()
      println(s"Memory data: ${dut.io.rdData.peekInt()}")
    }
  }

  "Initialized memory" should "pass" in {
    test(new InitMemory) { dut =>
      // Some defaults
      dut.io.rdAddr.poke(0.U)
      dut.io.wrEna.poke(false.B)
      dut.io.wrAddr.poke(0.U)
      dut.io.wrData.poke(0.U)

      // Check all the initialization values are as expected
      val lines = scala.io.Source.fromFile("./src/main/resources/init.hex")
        .getLines().map { Integer.parseInt(_, 16) }.toSeq
      for (i <- 0 until 1024) {
        dut.io.rdAddr.poke(i.U)
        dut.clock.step()
        dut.io.rdData.expect(lines(i).U)
      }
    }
  }

  "Memory with forwarding" should "pass" in {
    test(new ForwardingMemory) { dut =>
      // Fill the memory
      dut.io.wrEna.poke(true.B)
      for (i <- 0 to 10) {
        dut.io.wrAddr.poke(i.U)
        dut.io.wrData.poke((i*10).U)
        dut.clock.step()
      }
      dut.io.wrEna.poke(false.B)

      dut.io.rdAddr.poke(10.U)
      dut.clock.step()
      dut.io.rdData.expect(100.U)
      dut.io.rdAddr.poke(5.U)
      dut.io.rdData.expect(100.U)
      dut.clock.step()
      dut.io.rdData.expect(50.U)

      // Same address read and write
      dut.io.wrAddr.poke(20.U)
      dut.io.wrData.poke(123.U)
      dut.io.wrEna.poke(true.B)
      dut.io.rdAddr.poke(20.U)
      dut.clock.step()
      dut.io.rdData.expect(123.U)
      println(s"Memory data: ${dut.io.rdData.peekInt()}")
    }
  }
}
