package model.events

import model.{Field, GameState}

object EventHandlerChain:

  private val moneyHandler = MoneyHandler()
  private val chestHandler = ChestHandler()
  private val chanceHandler = ChanceHandler()
  private val goToJailHandler = GoToJailHandler()

  moneyHandler
    .setNext(chestHandler)
    .setNext(chanceHandler)
    .setNext(goToJailHandler)

  def handle(event: Field, gameState: GameState): Option[GameState] =
    moneyHandler.handle(event, gameState)
