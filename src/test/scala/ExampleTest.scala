//- start scalatest_hello_world
import org.scalatest._

class ExampleTest extends FlatSpec with Matchers {
  "Integers" should "add" in {
    val i = 2
    val j = 3
    i + j should be (5)
  }
}
//- end