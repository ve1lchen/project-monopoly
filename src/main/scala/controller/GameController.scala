package controller

import model.FieldType.Business
import model.{Dice, DummyBoard, RandomDice}
import model.GameState

import scala.io.StdIn

class GameController(
    initialState: GameState,
    dice: Dice
                    ):

  private var state: GameState = initialState
  private val board: DummyBoard = state.board

  def processField(position: Int): Unit =
    val field = board.getField(position)
    if !state.propertyMap.contains(position) then
      println(s"Field ${field.name} has no owner.\n")

  def playTurn(): Unit =
    val steps: (Int, Int) = dice.roll()
    println(s"${state.getCurrentPlayer.name} rolled: ${steps._1}, ${steps._2}")
    state = state.movePlayer(steps)
    processField(state.getCurrentPlayer.position)
    state = state.nextTurn(steps._2 == steps._1)

  def buyProperty(): Unit =
    val player = state.getCurrentPlayer
    val field = state.board.getField(player.position)
    if field.fieldType == Business && player.money >= field.cost && !state.propertyMap.contains(player.position) then
      state = state.copy(players = state.players.updated(state.currentPlayerIndex, state.players(state.currentPlayerIndex).pay(field.cost)))
      state = state.updatePropertyMap(player.position, state.currentPlayerIndex)

  def getState: GameState =
    state