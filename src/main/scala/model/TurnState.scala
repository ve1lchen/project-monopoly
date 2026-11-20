package model

trait TurnState:
  def roll(
      state: GameState,
      dice: Dice
  ): Option[GameState]

  def endTurn(
      state: GameState
  ): Option[GameState]
