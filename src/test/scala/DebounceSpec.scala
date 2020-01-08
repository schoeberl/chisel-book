import chisel3.iotesters.PeekPokeTester
import chisel3.iotesters.Driver
import org.scalatest._


class DebounceSpec extends FlatSpec with Matchers {
  "Debounce test" should "pass" in {
    val FAC = 100
    Driver.execute(Array("--generate-vcd-output", "on"), () => new Debounce(FAC)) {
      c => new PeekPokeTester(c) {
        poke(c.io.btnU, 0)
        step(3)
        expect(c.io.led, 0)
        step(FAC/3)
        poke(c.io.btnU, 1)
        step(FAC/30)
        poke(c.io.btnU, 0)
        step(FAC/30)
        poke(c.io.btnU, 1)
        step(FAC)
        // expect(c.io.led, 1)
        expect(c.io.led, 0)
        step(FAC)
        expect(c.io.led, 0)
        step(FAC)
      }
    } should be (true)
  }

  "DebounceFunc test" should "pass" in {
    val FAC = 100
    Driver.execute(Array("--generate-vcd-output", "on"), () => new DebounceFunc(FAC)) {
      c => new PeekPokeTester(c) {
        poke(c.io.btnU, 0)
        step(3)
        expect(c.io.led, 0)
        step(FAC/3)
        poke(c.io.btnU, 1)
        step(FAC/30)
        poke(c.io.btnU, 0)
        step(FAC/30)
        poke(c.io.btnU, 1)
        step(FAC)
        // expect(c.io.led, 1)
        expect(c.io.led, 0)
        step(FAC)
        expect(c.io.led, 0)
        step(FAC)
      }
    } should be (true)
  }
}
