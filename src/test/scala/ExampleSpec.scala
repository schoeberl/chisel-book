//- start scalatest_hello_world
//- 开始 scala测试的hello world
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {

  "Integers" should "add" in {
    val i = 2
    val j = 3
    i + j should be (5)
  }
}
//- end
//- 结束
