package model.events

import model.{ChanceField, Field, GameState}

class ChanceHandler extends BaseEventHandler:
  override protected def handleEvent(event: Field, gameState: GameState): Option[GameState] =

    event match 
      case ChanceField => Some(gameState)
      case _ => None
