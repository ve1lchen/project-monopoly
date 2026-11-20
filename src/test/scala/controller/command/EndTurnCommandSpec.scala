package controller.command

import controller.GameController
import model.{AwaitingActionState, FixedDice, GameState, Player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class EndTurnCommandSpec extends AnyWordSpec with Matchers:

  "EndTurnCommand" should:
    "end the current player's turn" in:
      val state = GameState(
        players = Vector(Player("Alice"), Player("Renee")),
        turnState = AwaitingActionState
      )
      val controller = GameController(state, new FixedDice((2, 3)))
      val command    = EndTurnCommand(controller)

      command.execute()

      controller.getState.currentPlayerIndex shouldBe 1
      controller.getState.turnState shouldBe model.AwaitingRollState
