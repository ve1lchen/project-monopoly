package controller.command

import controller.GameController
import model.{FixedDice, GameState, Player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class RollDiceCommandSpec extends AnyWordSpec with Matchers:

  "RollDiceCommand" should:
    "execute a dice roll through the controller" in:
      val dice = new FixedDice((2, 3))
      val state = GameState(
        players = Vector(Player("Alice", id=0), Player("Renee", id=1))
      )
      val controller = GameController(state, dice)
      val command = RollDiceCommand(controller)

      command.execute()

      controller.getState.players.head.position shouldBe 5