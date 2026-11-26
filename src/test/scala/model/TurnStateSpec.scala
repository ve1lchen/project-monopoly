package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TurnStateSpec extends AnyWordSpec with Matchers:

  private def initialState: GameState =
    GameState(
      Vector(
        Player("Alice", id = 0),
        Player("Renee", id = 1)
      )
    )

  "AwaitingRollState" should:

    "allow rolling dice" in:
      val state  = initialState
      val result = AwaitingRollState.roll(state, (2, 3))

      result shouldBe defined
      result.get.turnState shouldBe AwaitingActionState
      result.get.getCurrentPlayer.position shouldBe 5

    "reject ending the turn" in:
      AwaitingRollState.endTurn(initialState) shouldBe None

    "reject undoing the roll" in:
      AwaitingRollState.undoRoll(initialState, (1, 1)) shouldBe None

  "AwaitingActionState" should:

    "reject rolling dice" in:
      val state = initialState.copy(turnState = AwaitingActionState)

      AwaitingActionState.roll(
        state,
        (2, 3)
      ) shouldBe None

    "allow ending the turn" in:
      val state = initialState.copy(
        turnState = AwaitingActionState,
        lastRoll = Some((0, (2, 3)))
      )

      val result = AwaitingActionState.endTurn(state)

      result shouldBe defined
      result.get.currentPlayerIndex shouldBe 1
      result.get.turnState shouldBe AwaitingRollState

    "grant an extra turn after doubles" in:
      val state = initialState.copy(
        turnState = AwaitingActionState,
        lastRoll = Some((0, (3, 3)))
      )

      val result = AwaitingActionState.endTurn(state)

      result shouldBe defined
      result.get.currentPlayerIndex shouldBe 0
      result.get.turnState shouldBe AwaitingRollState

    "reject undoing the turn" in:
      AwaitingActionState.undoEndTurn(initialState) shouldBe None