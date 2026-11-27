package model.events

import model.{ChestField, Field, GameState}

class ChestHandler extends BaseEventHandler:
  override protected def handleEvent(event: Field, gameState: GameState): Option[GameState] =

    event match
      case ChestField => Some(gameState)
      case _ => None
