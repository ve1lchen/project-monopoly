package model.events

import model.{Field, GameState, MoneyField}

class MoneyHandler extends BaseEventHandler:
  override protected def handleEvent(event: Field, gameState: GameState): Option[GameState] =

    event match
      case field: MoneyField => Some(gameState)
      case _ => None
