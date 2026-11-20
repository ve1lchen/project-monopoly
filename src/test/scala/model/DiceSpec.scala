package model

import model.{FixedDice, RandomDice}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

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

    "reject invalid dice values" in :
      val dice = new FixedDice((3, 4))

      val invalidRolls = Seq(
        (0, 3),
        (7, 3),
        (3, 0),
        (3, 7)
      )

      for invalid <- invalidRolls do
        intercept[IllegalArgumentException] {
          dice.setValues(invalid)
        }

        dice.roll() shouldBe(3, 4)

    "accept boundary values for both dice" in :
      val dice = new FixedDice((3, 4))

      val validRolls = Seq(
        (1, 3),
        (6, 3),
        (3, 1),
        (3, 6)
      )

      for values <- validRolls do
        dice.setValues(values)
        dice.roll() shouldBe values