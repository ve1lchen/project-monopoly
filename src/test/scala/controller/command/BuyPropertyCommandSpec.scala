package controller.command

import controller.GameController
import model.{FixedDice, GameState, Player, PropertyField, StandardBoard}
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

    "use first correct player" in:
      val player = Player("Anne", position = 1)
      val state = GameState(Vector(player, player))

      val controller = GameController(state)
      val command    = BuyPropertyCommand(controller)

      command.execute()

      val players = controller.getState.players
      players(0).money shouldBe player.money - StandardBoard.get(1)
        .asInstanceOf[PropertyField].price
      players(1).money shouldBe player.money