package controller.command

import controller.GameController
import model.{FixedDice, GameState, Player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class BuyPropertyCommandSpec extends AnyWordSpec with Matchers:

  "BuyPropertyCommand" should:
    "execute without changing the state when the current field cannot be purchased" in:
      val state = GameState(
        players = Vector(Player("Alice", id = 0), Player("Bob", id = 1))
      )
      val controller = GameController(state, new FixedDice((2, 3)))
      val command    = BuyPropertyCommand(controller)

      command.execute()

      controller.getState shouldBe state
