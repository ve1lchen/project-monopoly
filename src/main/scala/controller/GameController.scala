package controller

import model.ResultStatus.{AlreadyOwned, InsufficientFunds, NotPurchasable, OwnerIsYou, Success}
import model.FieldType.{Business}
import model.TurnPhase.turnPhases.{AwaitingAction, AwaitingRoll}
import model.{ResultStatus, Dice, DummyBoard, DummyField, GameState, Player}

class GameController(
    initialState: GameState,
    dice: Dice
                    ):

  private var state: GameState = initialState
  private val board: DummyBoard = state.board

  /*
  Rolls dices -> saves the travel -> processes the field
   */
  def rollDice(): Unit =
    if state.turnPhase == AwaitingRoll then
      val rolled = dice.roll()
      state = state.movePlayer(rolled)


  /*
  Ends Turn only after roll
   */
  def endTurn(): Unit =
    if state.turnPhase == AwaitingAction then
      state = state.nextTurn(state.lastRoll._1 == state.lastRoll._2)


  def buyProperty(): ResultStatus =
    val player = state.getCurrentPlayer
    val field = state.board.getField(player.position)

    state.propertyMap.get(field) match
      case Some(ownerId) if ownerId == state.getCurrentPlayer.id => OwnerIsYou
      case Some(_) => AlreadyOwned
      case None if field.fieldType != Business => NotPurchasable
      case None if player.money < field.cost => InsufficientFunds
      case None =>
        val updatedPlayers = state.players.updated(state.currentPlayerIndex, player.pay(field.cost))
        state = state.copy(players = updatedPlayers)
          .updatePropertyMap(field, player)
        Success

  def getState: GameState =
    state

  def getCurrentField(player: Player): DummyField =
    state.board.getField(player.position)

  def getCurrentFieldOwner: Option[Player] =
    val field = getCurrentField(state.getCurrentPlayer)
    state.propertyMap.get(field).flatMap: id =>
      state.players.find(_.id == id)