import internal.Dice
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class DiceSpec extends AnyWordSpec with Matchers:
  "A Dice" should:
    "return a number between 1 and 6" in:
      val result = Dice.roll()
      result._1 should ((be >= 1 and be <= 6))
      result._2 should ((be >= 1 and be <= 6))