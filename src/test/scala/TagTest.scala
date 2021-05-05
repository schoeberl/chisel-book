import org.scalatest._
//- start tag_test
object Unnecessary extends Tag("Unnecessary")

class TagTest extends FlatSpec with Matchers {
  "Integers" should "add" taggedAs(Unnecessary) in {
    17 + 25 should be (42)
  }
}
//- end
