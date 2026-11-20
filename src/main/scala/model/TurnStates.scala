package model

object AwaitingRollState extends TurnState:

  override def roll(state: GameState, dice: Dice): Option[GameState] =
    Some(state.movePlayer(dice.roll()))

  override def endTurn(
      state: GameState
  ): Option[GameState] =
    None

object AwaitingActionState extends TurnState:

  override def roll(state: GameState, dice: Dice): Option[GameState] =
    None

  override def endTurn(
      state: GameState
  ): Option[GameState] =
    val isDouble = state.lastRoll.exists { case (_, (a, b)) =>
      a == b
    }

    Some(state.nextTurn(isDouble))
