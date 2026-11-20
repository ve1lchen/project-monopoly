package model

import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec with Matchers {
  
  "A board" when {
    "set with values" have {
      val vec = Vector(StartField(),FreeParkingField,ChestField,ChanceField)
      val board = MBoard()
      "the same" in {
        board.size shouldBe 4
        board.fields shouldBe vec
        for (int i = 0; i < vec.size; i++) {
          board.getAbsolute(i) shouldBe vec(i)
        }
      }
      "a circular get" in {
        for (int i = 0; i < vec.size; i ++) {
          board.get(i) shouldBe vec(i)
        }
        for (int i = vec.size; i < vec.size * 2; i++) {
          board.get(i) shouldBe vec(i - vec.size)
        }
      }
    }
    "set with an empty field" should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy MBoard(Vector[Field]())
      }
    }
  }
}