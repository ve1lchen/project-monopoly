import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MonopolySpec extends AnyWordSpec with Matchers:

  "A monopoly" should:
    "run the main method" in {
      monopoly.main(Array.empty[String])
    }