package model

import model.DevelopmentLevel.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class FieldSpec extends AnyWordSpec with Matchers:

  "A normal field" should:
    "have the default name Field" in:
      new Field {}.name shouldBe "Field"

  "A EventField" should:
    "have the default name Event" in:
      new EventField {}.name shouldBe "Event"

  "A OnEnterField" should:
    "have the default name Enter-Event" in:
      new OnEnterField {}.name shouldBe "Enter-Event"

  "A OnPassField" should :
    "have the default name Pass-Event" in:
      new OnPassField {}.name shouldBe "Pass-Event"

  "A MoneyField" should :
    "have the default name Money" in:
      new MoneyField[Unit]:
        def money(context: Unit): Option[Int] = None
      .name shouldBe "Money"

  "A GainMoneyField" should:
    "have the default name Gain Money" in:
      new GainMoneyField:
        override def amount: Int = 1
      .name shouldBe "Gain Money"

  "A LoseMoneyField" should:
    "have the default name Lose Money" in:
      new LoseMoneyField:
        override def amount: Int = 1
      .name shouldBe "Gain Money"

  "A ActionField" should:
    "have the default name Action" in:
      new ActionField {}.name shouldBe "Action"

  "A RentField" should:
    "have the default name Rent" in:
      new RentField[Unit]:
        protected def calculateRent(context: Unit): Option[Int] = None
      .name shouldBe "Rent"


  "Field validation" should:
    "throw an IllegalArgumentException when any field has an empty or blank name" in:
      val blankNames = Seq("", "   ", "\t\n")
  
      blankNames.foreach { invalidName =>
        an[IllegalArgumentException] should be thrownBy StartField(100, invalidName)
        an[IllegalArgumentException] should be thrownBy TaxField(100, invalidName)
        an[IllegalArgumentException] should be thrownBy UtilityField(1, invalidName)
        an[IllegalArgumentException] should be thrownBy StreetField(1, RentStructure(1, 2, 3, 4, 5, 6), 1, invalidName)
      }
  
    "preserve the exact error message when a name is blank" in:
      val exception = intercept[IllegalArgumentException]:
        StartField(100, "")
      exception.getMessage should include("A field-name cannot be empty.")


  "EventFields" should:
    "have the correct default names and trait hierarchy" in:
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
  
    "allow overriding default names" in:
      StartField(100, "Los").name shouldBe "Los"


  "TaxField" should:
    "store valid positive money amounts and default name" in:
      val tax = TaxField(200)
      tax.amount shouldBe 200
      tax.name shouldBe "Tax"
      tax shouldBe a[MoneyField[Unit]]
      tax shouldBe a[EventField]
  
    "allow a custom name alongside a valid money amount" in:
      val luxuryTax = TaxField(75, "Luxury Tax")
      luxuryTax.amount shouldBe 75
      luxuryTax.name shouldBe "Luxury Tax"
  
    "throw an IllegalArgumentException when amount is 0 or negative" in:
      val message = "Money lost must be more than 0."
      val zeroException = intercept[IllegalArgumentException]:
        TaxField(0)
      zeroException.getMessage should include(message)
  
      val negativeException = intercept[IllegalArgumentException]:
        TaxField(-50)
      negativeException.getMessage should include(message)

  "StartField" should:
    "throw an IllegalArgumentException when amount is 0 or negative" in:
      val message = "Money gained must be more than 0."
      val zeroException = intercept[IllegalArgumentException]:
        StartField(0)
      zeroException.getMessage should include(message)

      val negativeException = intercept[IllegalArgumentException]:
        StartField(-50)
      negativeException.getMessage should include(message)


  "EventlessFields" should:
    "have the correct default names and trait hierarchy" in:
      val jail = JailField
      jail.name shouldBe "Jail"
      jail shouldBe a[EventlessField]
      jail shouldBe a[Field]
  
      val freeParking = FreeParkingField
      freeParking.name shouldBe "Free Parking"
      freeParking shouldBe a[EventlessField]
      freeParking shouldBe a[Field]


  "Pattern matching on sealed traits" should:
    "be exhaustive and distinguish between EventField and EventlessField" in:
      def categorize(field: Field): String = field match
        case _: OnPassField => "OnPass"
        case _: MoneyField[?] => "Money"
        case _: EventField => "Event"
        case _: EventlessField => "Eventless"
  
      categorize(TaxField(100)) shouldBe "Money"
      categorize(StartField(100)) shouldBe "OnPass"
      categorize(JailField) shouldBe "Eventless"

  "A rent structure" when:
    "set with number" should:
      val rentStructure = RentStructure(100, 101, 102, 103, 104, 105)
      "have the same numbers" in:
        rentStructure.baseRent shouldBe 100
        rentStructure.oneHouse shouldBe 101
        rentStructure.twoHouses shouldBe 102
        rentStructure.threeHouses shouldBe 103
        rentStructure.fourHouses shouldBe 104
        rentStructure.hotel shouldBe 105

    "set to less than zero" should:
      "throw an IllegalArgumentException" in:
        val negativeException = intercept[IllegalArgumentException]:
          RentStructure(-1, 0, 0, 0, 0, 0)
        negativeException.getMessage should include("Rent needs to be at least 0 or higher.")

    "set in partially descending order" should:
      "throw an IllegalArgumentException" in:
        val descException = intercept[IllegalArgumentException]:
          RentStructure(100, 101, 1, 2, 3, 4)
        descException.getMessage should include("The rent needs to increase.")
  
  "A property field" when:
    "set to a price (less than) zero" should:
      "throw an IllegalArgumentException" in:
        val message = "The price needs to be at least 0."
        val zeroException = intercept[IllegalArgumentException]:
          UtilityField(0)
        zeroException.getMessage should include(message)

        val negativeException = intercept[IllegalArgumentException]:
          UtilityField(-50)
        negativeException.getMessage should include(message)


  "A street-field" when:
    val rent = RentStructure(100, 101, 102, 103, 104, 105)
    "initialized normally" should:
      val street = StreetField(666, rent, 1, "Street")
      
      "have correct trait hierarchy" in:
        street shouldBe a[PropertyField]
        street shouldBe a[RentField[DevelopmentLevel]]
      
      "have initialized values" in:
        street.houseCost shouldBe 1
        street.hotelCost shouldBe 1
        street.rentTable shouldBe rent

      "have the default name Street" in:
        street.name shouldBe "Street"

      "have money return rentTable values" in:
        street.money(Undeveloped) shouldBe Some(-rent.baseRent)
        street.money(OneHouse) shouldBe Some(-rent.oneHouse)
        street.money(TwoHouses) shouldBe Some(-rent.twoHouses)
        street.money(ThreeHouses) shouldBe Some(-rent.threeHouses)
        street.money(FourHouses) shouldBe Some(-rent.fourHouses)
        street.money(Hotel) shouldBe Some(-rent.hotel)

    "set to a negative houseCost" should:
      "throw an IllegalArgumentException" in:
        val negException = intercept[IllegalArgumentException]:
          StreetField(666, rent, -1, "Street")
        negException.getMessage should include("The house/hotel-cost must be higher than 0.")

    "set to a bigger house than hotelCost" should:
      "throw an IllegalArgumentException" in:
        val negException = intercept[IllegalArgumentException]:
          StreetField(666, rent, 2, hotelCost = 1)
        negException.getMessage should include("A hotel should cost the same or more than a house")

  
  "A StationField" should:
    val station = StationField(666)
    "be initialized with default values" in:
      station.name shouldBe "Station"
      station.rentTable shouldBe Vector(25, 50, 100, 200)
    
    "have correct trait hierarchy" in:
      station shouldBe a[PropertyField]
      station shouldBe a[RentField[Int]]
  
    "throw an IllegalArgumentException if the rentTable is empty" in:
      val emptyException = intercept[IllegalArgumentException]:
        StationField(200, rentTable = Vector.empty)
      emptyException.getMessage should include("The rentTable cannot be empty.")

    "return the correct rent based on the number of owned stations" in:
      val station = StationField(666)
      // 1 Station im Besitz -> Index 0 im rentTable
      station.money(1) shouldBe Some(-25)
      station.money(2) shouldBe Some(-50)
      station.money(4) shouldBe Some(-200)

    "return None if the player owns no stations or the count exceeds the rentTable" in:
      val station = StationField(666)
      // 0 Stationen (0 - 1 = -1 -> out of bounds)
      station.money(0) shouldBe None
      // 5 Stationen (5 - 1 = 4 -> out of bounds)
      station.money(5) shouldBe None

  
  "A UtilityField" should:
    val utility = UtilityField(150)
    "be initialized with default values" in:
      utility.name shouldBe "Utility"
      utility.rentTable shouldBe Vector(4, 10)

    "be initialized with price" in:
      utility.price shouldBe 150
    
    "have correct trait hierarchy" in:
      utility shouldBe a[PropertyField]
      utility shouldBe a[RentField[(Int, Int)]]
    
    "calculate rent correctly" in:
      // Würfelwurf 7, besitzt 1 Werk (Faktor 4) -> 7 * 4 = 28
      utility.money(1, 7) shouldBe Some(-28)
      // Würfelwurf 7, besitzt 2 Werke (Faktor 10) -> 7 * 10 = 70
      utility.money(2, 7) shouldBe Some(-70)

    "throw an IllegalArgumentException if the dice roll is 0 or negative" in:
      an[IllegalArgumentException] should be thrownBy:
        utility.money(1, 0)
      an[IllegalArgumentException] should be thrownBy:
        utility.money(2, -3)
