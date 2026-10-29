package model

import model.TurnPhase.turnPhases.{AwaitingAction, AwaitingRoll}

/*
Companion object
 */
object TurnPhase:
  enum turnPhases:
    case AwaitingRoll, AwaitingAction

case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[DummyField, Player] = Map.empty, // Field: Player
    board: DummyBoard = DummyBoard(),
    lastRoll: (Int, Int) = (0 , 0),
    turnPhase: TurnPhase.turnPhases = AwaitingRoll
):

  /*
  The game is over if there`s only 1 not bankrupted player left
   */
  def checkGameOver: GameState =
    var notBankrupts: Int = 0
    for player <- players do if !player.isBankrupt then notBankrupts += 1
    copy(isGameOver = notBankrupts == 1)

  /*
  Changes to the next Player and changes turnPhase = AwaitingRoll
   */
  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex
    else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex, turnPhase = AwaitingRoll)

  /*
  Changes player`s position and sets turnPhase = AwaitingAction
   */
  def movePlayer(rolled: (Int, Int)): GameState =
    val newPosition =
      (players(currentPlayerIndex).position + rolled._2 + rolled._1) % board.size
    val updatedPlayers = players.updated(
      currentPlayerIndex,
      players(currentPlayerIndex).changePosition(newPosition)
    )
    copy(players = updatedPlayers, lastRoll = rolled, turnPhase = AwaitingAction)

  /*
  Adds new field:player relation to the map
   */
  def updatePropertyMap(field: DummyField, player: Player): GameState =
    val updatedMap = propertyMap + (field -> player)
    copy(propertyMap = updatedMap)

  def getCurrentPlayer: Player =
    players(currentPlayerIndex)
