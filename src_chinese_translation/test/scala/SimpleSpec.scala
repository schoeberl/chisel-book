import org.scalatest._

//- start scalatest_simple
//- 开始简单scala测试
class SimpleSpec extends FlatSpec with Matchers {

  "Tester" should "pass" in {
    chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
      new Tester(c)
    } should be (true)
  }
}
//- end
//- 结束
