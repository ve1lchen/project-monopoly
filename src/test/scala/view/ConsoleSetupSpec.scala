package view

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets.UTF_8
import model.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConsoleSetupSpec extends AnyWordSpec with Matchers:
  private def runSetup(input: String): (Vector[Player], String) =
    val in = new ByteArrayInputStream(input.getBytes(UTF_8))
    val out = new ByteArrayOutputStream()

    val players = scala.Console.withIn(in) {
      scala.Console.withOut(out) {
        ConsoleSetup.askPlayers
      }
    }

    (players, out.toString(UTF_8))

  "ConsoleSetup" should:
    "read player names" in:
      val (players, output) = runSetup("2\nAlice\nRenee\n")

      players shouldBe Vector(
        Player(name = "Alice", id = 0),
        Player(name = "Renee", id = 1)
      )

      output should include("Enter players count:")
      output should include("Enter Player 1 name:")
      output should include("Enter Player 2 name:")

    "ask again after non-numeric input" in:
      val (players, output) = runSetup("abc\n2\nAlice\nRenee\n")

      players.map(_.name) shouldBe Vector("Alice", "Renee")
      output should include("Enter players count from 2 to 4.")

    "ask again after a number outside the allowed range" in:
      for invalidCount <- Vector(1, 5) do
        val (players, output) =
          runSetup(s"$invalidCount\n2\nAlice\nRenee\n")

        players.map(_.name) shouldBe Vector("Alice", "Renee")
        output should include("Enter players count from 2 to 4.")

    "show an error and ask again when a name is empty" in :
      val (players, output) = runSetup("2\n\nRenee\n2\nAlice\nRenee\n")

      output should include("Player names must not be empty.")
      players.map(_.name) shouldBe Vector("Alice", "Renee")