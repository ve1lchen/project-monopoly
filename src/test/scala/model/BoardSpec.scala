package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec with Matchers:
  
  "A board" when:
    "set with values" should:
      val vec = Vector(StartField(100), FreeParkingField, ChestField, ChanceField)
      val board = MBoard(vec)

      "have the same fields" in:
        board.size shouldBe 4
        board.fields shouldBe vec
        for (i <- vec.indices) do
          board.getAbsolute(i) shouldBe Some(vec(i))

      "support circular get" in:
        for (i <- vec.indices) do
          board.get(i) shouldBe vec(i)
        for (i <- vec.size until vec.size * 2) do
          board.get(i) shouldBe vec(i - vec.size)

    "set with an empty field" should:
      "throw an IllegalArgumentException" in:
        an[IllegalArgumentException] should be thrownBy MBoard(Vector[Field]())
