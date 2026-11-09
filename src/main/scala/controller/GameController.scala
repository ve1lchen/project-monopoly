package controller

import model.ResultStatus.{AlreadyOwned, InsufficientFunds, NotPurchasable, OwnerIsYou, Success}
import model.FieldType.Business
import model.TurnPhase.turnPhases.{AwaitingAction, AwaitingRoll}
import model.{Board, Dice, Field, GameState, Player, PropertyField, ResultStatus}

class GameController(
    initialState: GameState,
    dice: Dice
                    ):

  private var state: GameState = initialState
  private val board: Board = state.board
  private var observers: Vector[Observer] = Vector.empty

  def addObserver(observer: Observer): Unit =
    observers = observers :+ observer

  private def notifyObservers(): Unit =
    observers.foreach(_.update())

  /*
  Rolls dices -> saves the travel -> processes the field
   */
  def rollDice(): Unit =
    if state.turnPhase == AwaitingRoll then
      val rolled = dice.roll()
      state = state.movePlayer(rolled)
      notifyObservers()


  /*
  Ends Turn only after roll
   */
  def endTurn(): Unit =
    if state.turnPhase == AwaitingAction then
      val isDouble = state.lastRoll.exists { case (_, (a, b)) => a == b }
      state = state.nextTurn(isDouble)
      notifyObservers()


  def buyProperty(): ResultStatus =
    val player = state.getCurrentPlayer
    val fieldId = player.position

    getCurrentField match
      case Some(property: PropertyField) =>
        state.propertyMap.get(fieldId) match
          case Some(_) =>
            AlreadyOwned
          case None if player.money < property.price =>
            InsufficientFunds
          case None =>
            val updatedPlayers = state.players.updated(
              state.currentPlayerIndex,
              player.pay(property.price)
            )
            state = state.copy(
              players=updatedPlayers,
              propertyMap = state.propertyMap.updated(fieldId, player.id)
            )
            notifyObservers()
            Success
      case _ =>
        NotPurchasable


  def getState: GameState =
    state

  def getCurrentField: Option[Field] =
    val player = state.getCurrentPlayer
    state.board.getField(player.position)

  def getCurrentFieldOwner: Option[Player] =
    val fieldId = state.getCurrentPlayer.position
    state.propertyMap.get(fieldId).flatMap: id =>
      state.players.find(_.id == id)