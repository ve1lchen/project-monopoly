package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSetupSpec extends AnyWordSpec with Matchers:

  private val invalidCountMessage = "Players count must be from 2 to 4."
  private val emptyNameMessage = "Player names must not be empty."

  private def namesOf(count: Int): Vector[String] =
    Vector.tabulate(count)(i => s"Player $i")

  "GameSetup" should:

    "isValidPLayerCount" should:
      "accept counts from 2 to 4" in:
        for count <- 2 to 4 do
          GameSetup.isValidPLayerCount(count) shouldBe true

      "reject counts outside 2 to 4" in:
        for count <- Vector(-1, 0, 1, 5) do
          GameSetup.isValidPLayerCount(count) shouldBe false

    "createPlayers" should:
      "reject counts outside 2 to 4 with an error message" in:
        for count <- Vector(0, 1, 5) do
          GameSetup.createPlayers(namesOf(count)) shouldBe
            Left(invalidCountMessage)

      "reject empty and blank names" in:
        for blank <- Vector("", " ", "   ") do
          GameSetup.createPlayers(Vector("Alice", blank)) shouldBe
            Left(emptyNameMessage)

      "check the player count before the names" in:
        GameSetup.createPlayers(Vector(" ")) shouldBe
          Left(invalidCountMessage)

      "accept two and four players" in:
        for count <- Vector(2, 4) do
          GameSetup.createPlayers(namesOf(count)).isRight shouldBe true

      "create players with sequential IDs" in:
        GameSetup.createPlayers(Vector("Alice", "Renee")) shouldBe
          Right(Vector(
            Player(name = "Alice", id = 0),
            Player(name = "Renee", id = 1)
          ))