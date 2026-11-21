package model

import model.DevelopmentLevel.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class FieldSpec extends AnyWordSpec with Matchers:

  "Field validation" should {
    "throw an IllegalArgumentException when any field has an empty or blank name" in {
      val blankNames = Seq("", "   ", "\t\n")
  
      blankNames.foreach { invalidName =>
        an[IllegalArgumentException] should be thrownBy StartField(100, invalidName)
        an[IllegalArgumentException] should be thrownBy TaxField(100, invalidName)
        an[IllegalArgumentException] should be thrownBy UtilityField(1, invalidName)
        an[IllegalArgumentException] should be thrownBy StreetField(1, RentStructure(1, 2, 3, 4, 5, 6), 1, invalidName)
      }
    }
  
    "preserve the exact error message when a name is blank" in {
      val exception = intercept[IllegalArgumentException] {
        StartField(100, "")
      }
      exception.getMessage should include("A field-name cannot be empty.")
    }
  }
  
  "EventFields" should {
    "have the correct default names and trait hierarchy" in {
      val start = StartField(100)
      start.name shouldBe "Start"
      start shouldBe a[EventField]
      start shouldBe a[Field]
  
      val chest = ChestField
      chest.name shouldBe "Community Chest"
      chest shouldBe a[EventField]
  
      val chance = ChanceField
      chance.name shouldBe "Chance"
      chance shouldBe a[EventField]
  
      val goToJail = GoToJailField
      goToJail.name shouldBe "Go To Jail"
      goToJail shouldBe a[EventField]
    }
  
    "allow overriding default names" in {
      StartField(100, "Los").name shouldBe "Los"
    }
  }
  
  "TaxField" should {
    "store valid positive money amounts and default name" in {
      val tax = TaxField(200)
      tax.amount shouldBe 200
      tax.name shouldBe "Tax"
      tax.isInstanceOf[MoneyField[Unit]]
      tax.isInstanceOf[EventField]
    }
  
    "allow a custom name alongside a valid money amount" in {
      val luxuryTax = TaxField(75, "Luxury Tax")
      luxuryTax.amount shouldBe 75
      luxuryTax.name shouldBe "Luxury Tax"
    }
  
    "throw an IllegalArgumentException when money is 0 or negative" in {
      val message = "Money lost must be more than 0."
      val zeroException = intercept[IllegalArgumentException] {
        TaxField(0)
      }
      zeroException.getMessage should include(message)
  
      val negativeException = intercept[IllegalArgumentException] {
        TaxField(-50)
      }
      negativeException.getMessage should include(message)
    }
  }
  
  "EventlessFields" should {
    "have the correct default names and trait hierarchy" in {
      val jail = JailField
      jail.name shouldBe "Jail"
      jail shouldBe a[EventlessField]
      jail shouldBe a[Field]
  
      val freeParking = FreeParkingField
      freeParking.name shouldBe "Free Parking"
      freeParking shouldBe a[EventlessField]
      freeParking shouldBe a[Field]
    }
  }
  
  "Pattern matching on sealed traits" should {
    "be exhaustive and distinguish between EventField and EventlessField" in {
      def categorize(field: Field): String = field match
        case _: OnPassField => "OnPass"
        case _: MoneyField[?] => "Money"
        case _: EventField => "Event"
        case _: EventlessField => "Eventless"
  
      categorize(TaxField(100)) shouldBe "Money"
      categorize(StartField(100)) shouldBe "OnPass"
      categorize(JailField) shouldBe "Eventless"
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
      val street = StreetField(666, rent, 1, "Street")
      
      "have correct trait hierarchy" in {
        street shouldBe a[PropertyField]
        street shouldBe a[RentField[DevelopmentLevel]]
      }
      
      "have the same houseCost" in {
        street.houseCost should be(1)
      }
      "have the same hotelCost" in {
        street.hotelCost should be(1)
      }
      "have the same RentStructure" in {
        street.rentTable should be(rent)
      }
      "have the name Street" in {
        street.name should be("Street")
      }
      "have money return rentTable" in {
        street.money(Undeveloped).get shouldBe -rent.baseRent
        street.money(OneHouse).get shouldBe -rent.oneHouse
        street.money(TwoHouses).get shouldBe -rent.twoHouses
        street.money(ThreeHouses).get shouldBe -rent.threeHouses
        street.money(FourHouses).get shouldBe -rent.fourHouses
        street.money(Hotel).get shouldBe -rent.hotel
      }
    }
    "set houseCost=-1 and hotelCost=2 " should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy
          StreetField(666, rent, -1, "")
      }
    }
    "set to houseCost=2 and hotelCost=1 " should {
      "throw an IllegalArgumentException" in {
        an[IllegalArgumentException] should be thrownBy
          StreetField(666, rent, 2, "", 1)
      }
    }
  }
  
  "A StationField" should {
    val station = StationField(666)
    "be initialized with default values" in {
      station.name shouldBe "Station"
      station.rentTable shouldBe Vector(25, 50, 100, 200)
    }
    
    "have correct trait hierarchy" in {
      station shouldBe a[PropertyField]
      station.isInstanceOf[RentField[Int]]
    }
  
    "throw an IllegalArgumentException if the rentTable is empty" in {
      an[IllegalArgumentException] should be thrownBy {
        StationField(200, "", Vector.empty)
      }
    }
    "return the correct rent based on the number of owned stations" in {
      val station = StationField(666)
      // 1 Station im Besitz -> Index 0 im rentTable (25)
      station.money(1) shouldBe Some(-25)
      station.money(2) shouldBe Some(-50)
      station.money(4) shouldBe Some(-200)
    }
    "return None if the player owns no stations or the count exceeds the rentTable" in {
      val station = StationField(666)
      // 0 Stationen (0 - 1 = -1 -> out of bounds)
      station.money(0) shouldBe None
      // 5 Stationen (5 - 1 = 4 -> out of bounds)
      station.money(5) shouldBe None
    }
  }
  
  "A UtilityField" should {
    val utility = UtilityField(150)
    "be initialized with default values" in {
      utility.name shouldBe "Utility"
      utility.rentTable shouldBe Vector(4, 10)
    }

    "be initialized with price" in {
      utility.price shouldBe 150
    }
    
    "have correct trait hierarchy" in {
      utility.isInstanceOf[PropertyField]
      utility.isInstanceOf[RentField[(Int, Int)]]
    }
    
    "calculate rent correctly when owning only one utility" in {
      // Würfelwurf 7, besitzt nicht beide Werke (Faktor 4) -> 7 * 4 = 28
      utility.money(1, 7) shouldBe Some(-28)
    }
    "calculate rent correctly when owning both utilities" in {
      // Würfelwurf 7, besitzt beide Werke (Faktor 10) -> 7 * 10 = 70
      utility.money(2, 7) shouldBe Some(-70)
    }
    "throw an IllegalArgumentException if the dice roll is 0 or negative" in {
      an[IllegalArgumentException] should be thrownBy {
        utility.money(1, 0)
      }
      an[IllegalArgumentException] should be thrownBy {
        utility.money(2, -3)
      }
    }
  }
