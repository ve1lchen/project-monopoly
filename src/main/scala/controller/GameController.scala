package controller

import model.ResultStatus.*
import model.*


class GameController(
    initialState: GameState,
    dice: Dice
) extends Observable:

  private var state: GameState  = initialState
  private val board: Board      = state.board

  /**
   * Rolls dices -> saves the travel -> processes the field
   */
  def rollDice(): Unit =
    state.turnState
      .roll(state, dice)
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

  def buyProperty(): ResultStatus =
    val player  = state.getCurrentPlayer
    val fieldId = player.position

    state.propertyMap.get(fieldId) match
      case Some(ownerId) if ownerId == state.getCurrentPlayer.id => OwnerIsYou
      case Some(_)                                               => AlreadyOwned
      case None => state.board.get(player.position) match
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

  def getState: GameState =
    state
