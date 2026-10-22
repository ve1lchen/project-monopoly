import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class DiceSpec extends AnyWordSpec with Matchers:
  "A Dice" should:
    "return a number between 1 and 6" in:
      val result = Dice.roll()
      result should (be >= 1 and be <= 6)