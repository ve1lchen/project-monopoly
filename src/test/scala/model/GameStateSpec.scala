package model

import model.{GameState, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers:

  val players: Vector[Player] = Vector(
    Player("Bob"),
    Player("Rene"),
    Player("Alice")
  )
  val state = GameState(
    players = players,
    propertyMap = Map.empty
  )

  "A model.GameState" should:

    "switch to the next player" in:
      val updated = state.nextTurn(false)
      updated.currentPlayerIndex shouldBe 1

    "keep the current player on double" in:
      val updated = state.nextTurn(true)
      updated.currentPlayerIndex shouldBe 0

    "wrap around to the first player" in:
      val lastState = state.copy(currentPlayerIndex = 2)
      val updated = lastState.nextTurn(false)
      updated.currentPlayerIndex shouldBe 0

    "move the current player" in:
      val updated = state.movePlayer((4,4))
      updated.players(0).position shouldBe 8

    "check if the game is over" in:
      val bankruptedPlayers = Vector(Player("Alice", isBankrupt = true), Player("Bob"))
      val stateBankrupts = GameState(players = bankruptedPlayers, propertyMap = Map.empty)
      val updated = stateBankrupts.checkGameOver
      updated.isGameOver shouldBe true





