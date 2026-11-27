package model.events

import model.{Field, GameState, GoToJailField}

class GoToJailHandler extends BaseEventHandler:
  override protected def handleEvent(event: Field, gameState: GameState): Option[GameState] =

    event match
      case GoToJailField => Some(gameState)
      case _ => None
