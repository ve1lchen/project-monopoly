package model.events

import model.{Field, GameState, GoToJailField, JailField}

class GoToJailHandler extends BaseEventHandler:
  override protected def handleEvent(event: Field, gameState: GameState): Option[GameState] =

    event match
      case GoToJailField => Some(gameState.copy(
        players = gameState.players.updated(
          gameState.currentPlayerIndex,
          gameState.getCurrentPlayer.copy(
            inJail = true,
            position = gameState.board.fields.indexOf(JailField))
        )))

      case _ => None
