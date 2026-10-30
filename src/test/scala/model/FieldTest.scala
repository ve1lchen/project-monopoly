package model

import model.ActionType.{Start, Tax}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldTest extends AnyWordSpec with Matchers{
  "A basic field" when {
    "set to name=Test " should {
      val basicField = StandardField("Test")
      "have same name" in {
        basicField.name should be("Test")
      }
      "have Option[Action]=None" in {
        basicField.action should be(None)
      }
    }
    "set to name='Te st' and Option[Action]=Some(ActionType.Start) " should {
      val basicField = StandardField("Te st", Some(Start))
      "have same name" in {
        basicField.name should be("Te st")
      }
      "have Option[Action]=Some(ActionType.Start)" in {
        basicField.action should be(Some(Start))
      }
    }
    "initialized with an empty name " should {
      "throw an IllegalArgumentException" in {
        an [IllegalArgumentException] should be thrownBy StandardField("")
      }
    }
    "initialized with Tax(100) " should {
      val basicField = StandardField("Test", Some(Tax(100)))
      "have same tax" in {
        basicField.action.get should be (Tax(100))
      }
    }
  }

  "A rent structure" when {
    "set with number " should {
      val rentStructure = RentStructure(100, 101, 102, 103, 104, 105)
      "have the same numbers" in {
        rentStructure.baseRent should be(100)
        rentStructure.oneHouse should be(101)
        rentStructure.twoHouses should be(102)
        rentStructure.threeHouses should be(103)
        rentStructure.fourHouses should be(104)
        rentStructure.hotel should be(105)
      }
    }
    "set to less than zero " should {
      "throw an IllegalArgumentException" in {
        an [IllegalArgumentException] should be thrownBy
          RentStructure(-1, 0, 0, 0, 0, 0)
      }
    }
    "set in partially descending order " should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy
          RentStructure(100, 101, 1, 2, 3, 4)
      }
    }
  }

  "A property field" when {
    "set to a price less than zero " should {
      "throw an IllegalArgumentException" in {
        an [IllegalArgumentException] should be thrownBy UtilityField(-1)
      }
    }
  }

  "A street-field" when {
    val rent = RentStructure(100, 101, 102, 103, 104, 105)
    "set houseCost=1 and hotelCost=2 " should {
      val street = StreetField(666, rent, 1, 2)
      "have the same houseCost" in {
        street.houseCost should be(1)
      }
      "have the same hotelCost" in {
        street.hotelCost should be(2)
      }
      "have the same RentStructure" in {
        street.rentTable should be(rent)
      }
      "have the name Street" in {
        street.name should be("Street")
      }
    }
    "set houseCost=-1 and hotelCost=2 " should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy
          StreetField(666, rent, -1, 2)
      }
    }
    "set to houseCost=2 and hotelCost=1 " should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy
          StreetField(666, rent, 2, 1)
      }
    }
  }

  "A StationField" should {
    "be initialized with default values" in {
      val station = StationField(666)
      station.name shouldBe "Station"
      station.rentTable shouldBe Vector(25, 50, 100, 200)
    }

    "throw an IllegalArgumentException if the rentTable is empty" in {
      an[IllegalArgumentException] should be thrownBy {
        StationField(200, Vector.empty)
      }
    }
    "return the correct rent based on the number of owned stations" in {
      val station = StationField(666)
      // 1 Station im Besitz -> Index 0 im rentTable (25)
      station.getRent(1) shouldBe Some(25)
      station.getRent(2) shouldBe Some(50)
      station.getRent(4) shouldBe Some(200)
    }
    "return None if the player owns no stations or the count exceeds the rentTable" in {
      val station = StationField(666)
      // 0 Stationen (0 - 1 = -1 -> out of bounds)
      station.getRent(0) shouldBe None
      // 5 Stationen (5 - 1 = 4 -> out of bounds)
      station.getRent(5) shouldBe None
    }
  }

  "A UtilityField" should {
    "be initialized with default values" in {
      val utility = UtilityField(150)
      utility.name shouldBe "Utility"
    }
    "calculate rent correctly when owning only one utility" in {
      val utility = UtilityField(666)
      // Würfelwurf 7, besitzt nicht beide Werke (Faktor 4) -> 7 * 4 = 28
      utility.getRent(false, 7) shouldBe 28
    }
    "calculate rent correctly when owning both utilities" in {
      val utility = UtilityField(666)
      // Würfelwurf 7, besitzt beide Werke (Faktor 10) -> 7 * 10 = 70
      utility.getRent(true, 7) shouldBe 70
    }
    "throw an IllegalArgumentException if the dice roll is 0 or negative" in {
      val utility = UtilityField(666)
      an[IllegalArgumentException] should be thrownBy {
        utility.getRent(false, diceRoll = 0)
      }
      an[IllegalArgumentException] should be thrownBy {
        utility.getRent(true, -3)
      }
    }
  }
}
