package model

object AwaitingRollState extends TurnState:
  

  override def roll(state: GameState, diceRoll: (Int, Int)): Option[GameState] =
    Some(state.movePlayer(diceRoll))

  override def undoRoll(state: GameState, diceRoll: (Int, Int)): Option[GameState] =
    None

  override def endTurn(
      state: GameState
  ): Option[GameState] =
    None

  override def undoEndTurn(
      state: GameState
  ): Option[GameState] =
    val isDouble = state.lastRoll.exists { case (_, (a, b)) =>
      a == b
    }

    Some(state.undoNextTurn(isDouble))

object AwaitingActionState extends TurnState:

  override def roll(state: GameState, diceRoll: (Int, Int)): Option[GameState] =
    None

  override def undoRoll(state: GameState, diceRoll: (Int, Int)): Option[GameState] =
    Some(state.undoMovePlayer(diceRoll))

  override def endTurn(
      state: GameState
  ): Option[GameState] =
    val isDouble = state.lastRoll.exists { case (_, (a, b)) =>
      a == b
    }

    Some(state.nextTurn(isDouble))

  override def undoEndTurn(
      state: GameState
  ): Option[GameState] =
    None
    
