package model

import model.DevelopmentLevel.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RentStrategySpec extends AnyWordSpec with Matchers:
  
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

    "set to zero" should:
      "not throw an exception" in:
        noException shouldBe thrownBy:
          RentStructure(0, 0, 0, 0, 0, 0)

    "set in partially descending order" should:
      "throw an IllegalArgumentException" in:
        val descException = intercept[IllegalArgumentException]:
          RentStructure(100, 101, 1, 2, 3, 4)
        descException.getMessage should include("The rent needs to increase.")

    "set in a partially non-increasing order" should:
      "not throw an exception" in:
        noException shouldBe thrownBy:
          RentStructure(0, 0, 0, 0, 0, 0)


  "StreetRentStrategy" should:
    val rent = RentStructure(0, 1, 2, 3, 4, 5)
    val street = StreetRentStrategy(rent)
    "return constructor values" in:
      street.calculateRent(Undeveloped) shouldBe Some(rent.baseRent)
      street.calculateRent(OneHouse) shouldBe Some(rent.oneHouse)
      street.calculateRent(TwoHouses) shouldBe Some(rent.twoHouses)
      street.calculateRent(ThreeHouses) shouldBe Some(rent.threeHouses)
      street.calculateRent(FourHouses) shouldBe Some(rent.fourHouses)
      street.calculateRent(Hotel) shouldBe Some(rent.hotel)

  "StationRentStrategy" should:
    "return the correct rent based on the number of owned stations" in:
      val rentTable = Vector(25, 50, 100, 200)
      val stationVar = StationRentStrategy(rentTable)
      val stationDef = StationRentStrategy()
      // 1 Station im Besitz -> Index 0 im rentTable
      for (i <- rentTable.indices) do
        stationVar.calculateRent(i + 1) shouldBe Some(rentTable(i))
        stationDef.calculateRent(i + 1) shouldBe Some(rentTable(i))

    "throw an IllegalArgumentException if the rentTable is empty" in:
      val emptyException = intercept[IllegalArgumentException]:
        StationRentStrategy(Vector.empty)
      emptyException.getMessage should include("The rentTable cannot be empty.")

  "UtilityRentStrategy" should:
    "return the correct rent based on the dice roll" in:
      val vec = Vector(1, 2, 3, 4)
      val utility = UtilityRentStrategy(vec)
      for (i <- vec.indices)
        for (k <- 0 until 10)
          utility.calculateRent((i + 1, k)) shouldBe Some(vec(i) * k)


    "throw an IllegalArgumentException if the dice roll is negative" in:
      val negException = intercept[IllegalArgumentException]:
        UtilityRentStrategy().calculateRent((1, -1))
      negException.getMessage should include("A dice roll must be at least 0.")

    "not throw an exception if the dice roll is 0" in:
      noException shouldBe thrownBy:
        UtilityRentStrategy().calculateRent((2, 0))
        