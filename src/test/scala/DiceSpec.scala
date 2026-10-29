import model.{FixedDice, RandomDice}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class DiceSpec extends AnyWordSpec with Matchers:
  "A RandomDice" should:
    "return a number between 1 and 6" in:
      val result = RandomDice().roll()
      result._1 should ((be >= 1 and be <= 6))
      result._2 should ((be >= 1 and be <= 6))
    
  "A FixedDice" should:
    "return 5 and 5" in:
      val result = FixedDice((5,5)).roll()
      result._1 shouldBe 5
      result._2 shouldBe 5

    "set and roll new values" in:
      val dice = new FixedDice((1, 2))
      dice.setValues((3,4))
      val result = dice.roll()
      result._1 shouldBe 3
      result._2 shouldBe 4
      