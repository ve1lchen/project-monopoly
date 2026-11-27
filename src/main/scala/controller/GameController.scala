package controller

import model.ResultStatus.*
import model.*


case class GameController(
    initialState: GameState,
    dice: Dice = RandomDice(),
    undoManager: UndoManager = UndoManager()
) extends Observable:

  private var state: GameState  = initialState
  private val board: Board      = state.board

  /**
   * Rolls dices -> saves the travel -> processes the field
   */
  def rollDice(diceRoll : (Int, Int)): Unit =
    state.turnState
      .roll(state, diceRoll)
      .foreach: updatedState =>
        state = updatedState
        notifyObservers()

  def undoRollDice(diceRoll: (Int, Int)): Unit =
    state.turnState
      .undoRoll(state, diceRoll)
      .foreach: updatedState =>
        state = updatedState
        notifyObservers()

  /**
   * Ends Turn only after roll
   */
  def endTurn(): Unit =
    state.turnState
      .endTurn(state)
      .foreach: updatedState =>
        state = updatedState
        notifyObservers()

  def undoEndTurn(): Unit =
    state.turnState
      .undoEndTurn(state)
      .foreach: updatedState =>
        state = updatedState
        notifyObservers()

  def buyProperty(playerIndex: Int, field: Field): ResultStatus =
    val player  = state.players(playerIndex)
    val fieldId = player.position

    state.propertyMap.get(fieldId) match
      case Some(ownerId) if ownerId == state.getCurrentPlayer.id => OwnerIsYou
      case Some(_)                                               => AlreadyOwned
      case None => field match
        case property: PropertyField =>
          if player.money < property.price then
            InsufficientFunds
          else
            val updatedPlayers = state.players.updated(
              state.currentPlayerIndex,
              player.pay(property.price)
            )
            state = state
              .copy(players = updatedPlayers)
              .updatePropertyMap(fieldId, player.id)
            notifyObservers()
            Success
        case _ =>
          NotPurchasable

  def undoBuy(playerIndex: Int, field: Field): Unit =
    val player = state.players(playerIndex)
    val fieldId = state.board.fields.indexOf(field)
    field match
      case property: PropertyField
        if state.propertyMap.get(fieldId).contains(player.id) =>
        val updatedPlayers = state.players.updated(
          state.currentPlayerIndex,
          player.receive(property.price)
        )
        state = state
          .copy(players = updatedPlayers)
          .removePropertyMap(fieldId)
        notifyObservers()
      case _ => ()

  def getState: GameState =
    state
