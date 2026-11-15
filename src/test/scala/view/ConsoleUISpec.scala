package view

import controller.GameController
import model.TurnPhase.turnPhases.{AwaitingAction, AwaitingRoll}
import model.{FixedDice, GameState, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import view.ConsoleUI

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets.UTF_8

class ConsoleUISpec extends AnyWordSpec with Matchers:

  private def newState(): GameState =
    GameState(Vector(Player("Alice", id = 0), Player("Renee", id = 1)))

  /*
  Feeds the given lines to the UI as stdin and returns the controller (to check
  the resulting game state) and the printed text. The text is normalized: ANSI escape
  codes are removed, whitespace is collapsed and everything is lower-cased. So
  spaces, line breaks, colors and letter case in the layout do not matter.
  The input must end with "0", otherwise the UI loop never stops.
   */
  private def runUi(
      input: String,
      state: GameState = newState(),
      dice: (Int, Int) = (2, 3)
  ): (GameController, String) =
    val controller = GameController(state, new FixedDice(dice))
    val ui = ConsoleUI(controller)
    val in = new ByteArrayInputStream(input.getBytes(UTF_8))
    val out = new ByteArrayOutputStream()
    val printer = new PrintStream(out, true, UTF_8)

    scala.Console.withIn(in) {
      scala.Console.withOut(printer) {
        ui.run()
      }
    }
    (controller, normalize(out.toString(UTF_8)))

  private def normalize(raw: String): String =
    raw
      .replaceAll("\u001b\\[[0-9;]*[A-Za-z]", "")
      .replaceAll("\\s+", " ")
      .trim
      .toLowerCase

  "ConsoleUI" should:
    
    "display the ui" in:
      val (_, text) = runUi("0\n")

      text should include ("monopoly")
      text should include (">>")
      text should include ("field has no owner")

    "clear the screen before displaying the ui" in:
      val controller = GameController(newState(), new FixedDice((2, 3)))
      val ui = ConsoleUI(controller)
      val input = new ByteArrayInputStream("0\n".getBytes(UTF_8))
      val output = new ByteArrayOutputStream()
      val printer = new PrintStream(output, true, UTF_8)

      scala.Console.withIn(input) {
        scala.Console.withOut(printer) {
          ui.run()
        }
      }

      output.toString(UTF_8) should include ("\u001b[H\u001b[2J\u001b[3J")

    "show results when the user exits" in:
      val controller = GameController(
        GameState(Vector(Player("Alice"), Player("Renee"))),
        new FixedDice((2, 3))
      )
      val ui = ConsoleUI(controller)
      val input = new ByteArrayInputStream("0\n".getBytes(UTF_8))
      val output = new ByteArrayOutputStream()
      val printer = new PrintStream(output, true, UTF_8)

      scala.Console.withIn(input) {
        scala.Console.withOut(printer) {
          ui.run()
        }
      }

      val text = output.toString(UTF_8)
      text should include ("RESULTS")
      text should include ("Alice")
      text should include ("Renee")

    "print the names of all players when the user exits" in:
      val (_, text) = runUi("0\n")
      text should include ("alice")
      text should include ("renee")

    "show the current player and his money on the first screen" in:
      val (controller, text) = runUi("0\n")
      text should include ("alice")
      text should include ("1500")
      controller.getState shouldBe newState()

    "not change the game state when the user exits immediately" in:
      val (controller, _) = runUi("0\n")
      controller.getState.currentPlayerIndex shouldBe 0
      controller.getState.turnPhase shouldBe AwaitingRoll
      controller.getState.lastRoll shouldBe None

    "roll the dice and move the current player" in:
      val (controller, text) = runUi("1\n0\n")
      val state = controller.getState
      state.getCurrentPlayer.position shouldBe 5
      state.lastRoll shouldBe Some((0, (2, 3)))
      state.turnPhase shouldBe AwaitingAction
      text should include ("field5")

    "ignore a second roll in the same turn" in:
      val (controller, _) = runUi("1\n1\n0\n")
      controller.getState.getCurrentPlayer.position shouldBe 5

    "buy the current property" in:
      val (controller, text) = runUi("1\n2\n0\n")
      val state = controller.getState
      state.propertyMap shouldBe Map(5 -> 0)
      state.players(0).money shouldBe 1400
      text should include ("1400")

    "not charge the player when buying on a non-purchasable field" in:
      val (controller, _) = runUi("2\n0\n")
      controller.getState.propertyMap shouldBe empty
      controller.getState.players(0).money shouldBe 1500

    "not charge the player twice for the same property" in:
      val (controller, _) = runUi("1\n2\n2\n0\n")
      controller.getState.players(0).money shouldBe 1400
      controller.getState.propertyMap shouldBe Map(5 -> 0)

    "not let another player buy an owned property" in:
      val (controller, _) = runUi("1\n2\n3\n1\n2\n0\n")
      val state = controller.getState
      state.currentPlayerIndex shouldBe 1
      state.players(1).money shouldBe 1500
      state.propertyMap shouldBe Map(5 -> 0)

    "switch to the next player after a roll and end of turn" in:
      val (controller, _) = runUi("1\n3\n0\n")
      controller.getState.currentPlayerIndex shouldBe 1
      controller.getState.turnPhase shouldBe AwaitingRoll

    "remember who rolled after the turn ends" in:
      val (controller, text) = runUi("1\n3\n0\n")
      controller.getState.lastRoll shouldBe Some((0, (2, 3)))
      text should include ("last roll: alice")

    "keep the same player after a double" in:
      val (controller, _) = runUi("1\n3\n0\n", dice = (3, 3))
      controller.getState.currentPlayerIndex shouldBe 0
      controller.getState.turnPhase shouldBe AwaitingRoll

    "ignore end of turn before the dice were rolled" in:
      val (controller, _) = runUi("3\n0\n")
      controller.getState.currentPlayerIndex shouldBe 0
      controller.getState.turnPhase shouldBe AwaitingRoll

    "ignore unknown input and keep running" in:
      val (controller, text) = runUi("abc\n9\n\n0\n")
      controller.getState shouldBe newState()
      text should include ("alice")
      text should include ("renee")

    "show the money of every player in the results" in:
      val (_, text) = runUi("1\n2\n3\n0\n")
      text should include ("1400")
      text should include ("1500")

    "show input invitation" in:
      val (_, text) = runUi("0\n")
      text should include(">>")


    "show - if no one rolled yet" in :
      val (_, text) = runUi("0\n")
      text should include("last roll: -")

    "show properties in result screen" in :
      val (_, text) = runUi("1\n2\n3\n1\n3\n1\n2\n3\n0\n")
      text should include("5, 10")
      text should include("alice || 1300 || properties: 5, 10")
      text should include("renee || 1500 || properties:")
      text should not include ("1, 2, 3")
      text should include("------")

    "show field owner" in :
      val (_, text) = runUi("1\n2\n0\n")
      text should include("owner:")