package model

import model.DevelopmentLevel.{FourHouses, Hotel, OneHouse, ThreeHouses, TwoHouses, Undeveloped}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldTest extends AnyWordSpec with Matchers{

  import org.scalatest.wordspec.AnyWordSpec
  import org.scalatest.matchers.should.Matchers

  class FieldSpec extends AnyWordSpec with Matchers:

  "Field validation" should {
    "throw an IllegalArgumentException when any field has an empty or blank name" in {
      val blankNames = Seq("", "   ", "\t\n")

      blankNames.foreach { invalidName =>
        an[IllegalArgumentException] should be thrownBy StartField(invalidName)
        an[IllegalArgumentException] should be thrownBy ChestField(invalidName)
        an[IllegalArgumentException] should be thrownBy ChanceField(invalidName)
        an[IllegalArgumentException] should be thrownBy GoToJailField(invalidName)
        an[IllegalArgumentException] should be thrownBy TaxField(100, invalidName)
        an[IllegalArgumentException] should be thrownBy JailField(invalidName)
        an[IllegalArgumentException] should be thrownBy FreeParkingField(invalidName)
      }
    }

    "preserve the exact error message when a name is blank" in {
      val exception = intercept[IllegalArgumentException] {
        StartField("   ")
      }
      exception.getMessage should include("A field-name cannot be empty.")
    }
  }

  "EventFields" should {
    "have the correct default names and trait hierarchy" in {
      val start = StartField()
      start.name shouldBe "Start"
      start shouldBe a[EventField]
      start shouldBe a[Field]

      val chest = ChestField()
      chest.name shouldBe "Community Chest"
      chest shouldBe a[EventField]

      val chance = ChanceField()
      chance.name shouldBe "Chance"
      chance shouldBe a[EventField]

      val goToJail = GoToJailField()
      goToJail.name shouldBe "Go To Jail"
      goToJail shouldBe a[EventField]
    }

    "allow overriding default names" in {
      StartField("Los").name shouldBe "Los"
      ChestField("Gemeinschaftsfeld").name shouldBe "Gemeinschaftsfeld"
      ChanceField("Ereignisfeld").name shouldBe "Ereignisfeld"
      GoToJailField("Gehe in das Gefängnis").name shouldBe "Gehe in das Gefängnis"
    }
  }

  "TaxField" should {
    "store valid positive money amounts and default name" in {
      val tax = TaxField(200)
      tax.money shouldBe 200
      tax.name shouldBe "Tax"
      tax shouldBe a[MoneyField]
      tax shouldBe a[EventField]
    }

    "allow a custom name alongside a valid money amount" in {
      val luxuryTax = TaxField(75, "Luxury Tax")
      luxuryTax.money shouldBe 75
      luxuryTax.name shouldBe "Luxury Tax"
    }

    "throw an IllegalArgumentException when money is 0 or negative" in {
      val zeroException = intercept[IllegalArgumentException] {
        TaxField(0)
      }
      zeroException.getMessage should include("Tax must be higher than 0")

      val negativeException = intercept[IllegalArgumentException] {
        TaxField(-50)
      }
      negativeException.getMessage should include("Tax must be higher than 0")
    }
  }

  "EventlessFields" should {
    "have the correct default names and trait hierarchy" in {
      val jail = JailField()
      jail.name shouldBe "Jail"
      jail shouldBe a[EventlessField]
      jail shouldBe a[Field]

      val freeParking = FreeParkingField()
      freeParking.name shouldBe "Free Parking"
      freeParking shouldBe a[EventlessField]
      freeParking shouldBe a[Field]
    }

    "allow overriding default names" in {
      JailField("Just Visiting").name shouldBe "Just Visiting"
      FreeParkingField("Frei Parken").name shouldBe "Frei Parken"
    }
  }

  "Pattern matching on sealed traits" should {
    "be exhaustive and distinguish between EventField and EventlessField" in {
      def categorize(field: Field): String = field match
        case _: MoneyField => "Money"
        case _: EventField => "Event"
        case _: EventlessField => "Eventless"

      categorize(TaxField(100)) shouldBe "Money"
      categorize(StartField()) shouldBe "Event"
      categorize(JailField()) shouldBe "Eventless"
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
      
      "have correct trait hierarchy" in {
        street shouldBe a[PropertyField]
        street shouldBe a[RentField[DevelopmentLevel]]
      }
      
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
      "have getRent return rentTable" in {
        street.getRent(Undeveloped) should be(rent.baseRent)
        street.getRent(OneHouse) should be(rent.oneHouse)
        street.getRent(TwoHouses) should be(rent.twoHouses)
        street.getRent(ThreeHouses) should be(rent.threeHouses)
        street.getRent(FourHouses) should be(rent.fourHouses)
        street.getRent(Hotel) should be(rent.hotel)
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
    
    "have correct trait hierarchy" in {
      station shouldBe a[PropertyField]
      station shouldBe a[RentField]
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
    val utility = UtilityField(150)
    "be initialized with default values" in {
      
      utility.name shouldBe "Utility"
    }
    
    "have correct trait hierarchy" in {
      utility shouldBe a[PropertyField]
      utility shouldBe a[RentField]
    }
    
    "calculate rent correctly when owning only one utility" in {
      // Würfelwurf 7, besitzt nicht beide Werke (Faktor 4) -> 7 * 4 = 28
      utility.getRent(false, 7) shouldBe 28
    }
    "calculate rent correctly when owning both utilities" in {
      // Würfelwurf 7, besitzt beide Werke (Faktor 10) -> 7 * 10 = 70
      utility.getRent(true, 7) shouldBe 70
    }
    "throw an IllegalArgumentException if the dice roll is 0 or negative" in {
      an[IllegalArgumentException] should be thrownBy {
        utility.getRent(false, diceRoll = 0)
      }
      an[IllegalArgumentException] should be thrownBy {
        utility.getRent(true, -3)
      }
    }
  }
}
