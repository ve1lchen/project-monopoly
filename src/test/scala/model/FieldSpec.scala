package model

import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldSpec extends AnyWordSpec with Matchers:
  
  val rentF = RentFunctions(() => DevelopmentLevel.Undeveloped, () => 7)

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
      new MoneyField:
        def money(context: RentContext, functions: RentFunctions): Option[Int] = None
      .name shouldBe "Money"

  "A GainMoneyField" should:
    "have the default name Gain Money" in:
      val gainMoney = new GainMoneyField:
        override def amount: Int = 1
      gainMoney.name shouldBe "Gain Money"
      gainMoney.money(RentContext(), rentF) shouldBe Some(1)

  "A LoseMoneyField" should:
    "have the default name Lose Money" in:
      val loseMoney = new LoseMoneyField:
        override def amount: Int = 1
      loseMoney.name shouldBe "Lose Money"
      loseMoney.money(RentContext(), rentF) shouldBe Some(-1)

  "A EventlessField" should:
    "have the default name Normal Field" in:
      new EventlessField() {}.name shouldBe "Normal Field"

  "A ActionField" should:
    "have the default name Action" in:
      new ActionField {}.name shouldBe "Action"

  "A PropertyField" should:
    "have the default name Property" in:
      new PropertyField:
        override def price: Int = 0
      .name shouldBe "Property"

  "A RentField" should:
    "have the default name Rent" in:
      new RentField[Int]:
        val rentStrategy: RentStrategy = StationRentStrategy()
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
      tax shouldBe a[MoneyField]
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
        case _: MoneyField => "Money"
        case _: EventField => "Event"
        case _: EventlessField => "Eventless"
  
      categorize(TaxField(100)) shouldBe "Money"
      categorize(StartField(100)) shouldBe "OnPass"
      categorize(JailField) shouldBe "Eventless"
  
  "A property field" when:
    "set to a price less than zero" should:
      "throw an IllegalArgumentException" in:
        val negativeException = intercept[IllegalArgumentException]:
          UtilityField(-50, "Utility")
        negativeException.getMessage should include("The price needs to be at least 0.")

    "set to the price zero" should:
      "not throw an exception" in:
        noException shouldBe thrownBy:
          UtilityField(0, "Utility")


  "A StreetField" when:
    val rent = RentStructure(100, 101, 102, 103, 104, 105)
    "initialized normally" should:
      val street = StreetField(666, StreetRentStrategy(rent), 1, hotelCost = 1)
      StreetField(1, StreetRentStrategy(rent), 1, "Street")
      
      
      "have correct trait hierarchy" in:
        street shouldBe a[PropertyField]
        street shouldBe a[RentField[DevelopmentLevel]]
      
      "have initialized values" in:
        street.houseCost shouldBe 1
        street.hotelCost shouldBe 1
        street.rentStrategy.asInstanceOf[StreetRentStrategy]
          .rentTable shouldBe rent

      "have the default name Street" in:
        street.name shouldBe "Street"

    "set to a zero or negative houseCost" should:
      "throw an IllegalArgumentException" in:
        val message = "The house/hotel-cost must be higher than 0."
        val zeroException = intercept[IllegalArgumentException]:
          StreetField(666, rent, 0, "Street", 1)
        zeroException.getMessage should include(message)

        val negException = intercept[IllegalArgumentException]:
          StreetField(666, rent, -1, "Street")
        negException.getMessage should include(message)

    "set to a zero or negative hotelCost" should:
      "throw an IllegalArgumentException" in:
        val message = "The house/hotel-cost must be higher than 0."
        val zeroException = intercept[IllegalArgumentException]:
          StreetField(666, rent, 1, "Street", 0)
        zeroException.getMessage should include(message)

        val negException = intercept[IllegalArgumentException]:
          StreetField(666, rent, 1, "Street", -1)
        negException.getMessage should include(message)

    "set to a bigger house than hotelCost" should:
      "throw an IllegalArgumentException" in:
        val negException = intercept[IllegalArgumentException]:
          StreetField(666, rent, 2, "Street", 1)
        negException.getMessage should include("A hotel should cost the same or more than a house")

    "set to the same house/hotel cost" should:
      "not throw an exception" in:
        noException shouldBe thrownBy:
          StreetField(0, rent, 1, "Street", 1)

    "set to different house/hotel costs" should:
      "not throw an exception" in:
        noException shouldBe thrownBy:
          StreetField(0, rent, 1, "Street", 2)

  
  "A StationField" should:
    val station = StationField(666, rentStrategy = StationRentStrategy())
    "be initialized with default values" in:
      station.name shouldBe "Station"
      station.rentStrategy.asInstanceOf[StationRentStrategy]
        .rentTable shouldBe Vector(25, 50, 100, 200)

      StationField(1, "Station", StationRentStrategy().rentTable)
    
    "have correct trait hierarchy" in:
      station shouldBe a[PropertyField]
      station shouldBe a[RentField[Int]]

    "return the correct rent based on the number of owned stations" in:
      val rentTable = Vector(25, 50, 100, 200)
      val station = StationField(666, "Station")
      // 1 Station im Besitz -> Index 0 im rentTable
      for (i <- rentTable.indices) do {
        station.money(RentContext(i + 1), rentF) shouldBe Some(-rentTable(i))
      }

    "return None if the player owns no stations or the count exceeds the rentTable" in:
      val station = StationField(666, "Station")
      // 0 Stationen (0 - 1 = -1 -> out of bounds)
      station.money(RentContext(), rentF) shouldBe None
      // 5 Stationen (5 - 1 = 4 -> out of bounds)
      station.money(RentContext(5), rentF) shouldBe None

  
  "A UtilityField" should:
    val utility = UtilityField(150, rentStrategy = UtilityRentStrategy())

    UtilityField(1, "Utility", UtilityRentStrategy().rentTable)
    "be initialized with default values" in:
      utility.name shouldBe "Utility"
      utility.rentStrategy.asInstanceOf[UtilityRentStrategy]
        .rentTable shouldBe Vector(4, 10)

    "be initialized with price" in:
      utility.price shouldBe 150
    
    "have correct trait hierarchy" in:
      utility shouldBe a[PropertyField]
      utility shouldBe a[RentField[(Int, Int)]]
    
    "calculate rent correctly" in:
      // Würfelwurf 7, besitzt 1 Werk (Faktor 4) -> 7 * 4 = 28
      utility.money(RentContext(1, 1), rentF) shouldBe Some(-28)
      // Würfelwurf 7, besitzt 2 Werke (Faktor 10) -> 7 * 10 = 70
      utility.money(RentContext(2, 2), rentF) shouldBe Some(-70)
