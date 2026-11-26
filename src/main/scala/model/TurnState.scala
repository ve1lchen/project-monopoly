package model

trait TurnState:
  def roll(
      state: GameState,
      diceRoll: (Int, Int)
  ): Option[GameState]

  def endTurn(
      state: GameState
  ): Option[GameState]
  
  def undoEndTurn(
      state: GameState
  ): Option[GameState]
  
  def undoRoll(
      state: GameState,
      diceRoll: (Int, Int)
  ): Option[GameState]
