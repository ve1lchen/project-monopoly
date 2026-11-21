package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RentStrategySpec extends AnyWordSpec with Matchers:
  
  "A rent structure" when :
    "set with number" should :
      val rentStructure = RentStructure(100, 101, 102, 103, 104, 105)
      "have the same numbers" in :
        rentStructure.baseRent shouldBe 100
        rentStructure.oneHouse shouldBe 101
        rentStructure.twoHouses shouldBe 102
        rentStructure.threeHouses shouldBe 103
        rentStructure.fourHouses shouldBe 104
        rentStructure.hotel shouldBe 105

    "set to less than zero" should :
      "throw an IllegalArgumentException" in :
        val negativeException = intercept[IllegalArgumentException]:
          RentStructure(-1, 0, 0, 0, 0, 0)
        negativeException.getMessage should include("Rent needs to be at least 0 or higher.")

    "set to zero" should :
      "not throw an exception" in :
        noException shouldBe thrownBy:
          RentStructure(0, 0, 0, 0, 0, 0)

    "set in partially descending order" should :
      "throw an IllegalArgumentException" in :
        val descException = intercept[IllegalArgumentException]:
          RentStructure(100, 101, 1, 2, 3, 4)
        descException.getMessage should include("The rent needs to increase.")

    "set in a partially non-increasing order" should :
      "not throw an exception" in :
        noException shouldBe thrownBy:
          RentStructure(0, 0, 0, 0, 0, 0)