package model.events

import model.{Field, GameState}

trait EventHandler:
  def handle(event: Field, gameState: GameState): Option[GameState]

  def setNext(handler: EventHandler): EventHandler


abstract class BaseEventHandler extends EventHandler:

  private var nextHandler: Option[EventHandler] = None

  override def setNext(handler: EventHandler): EventHandler =
    nextHandler = Some(handler)
    handler

  override def handle(event: Field, gameState: GameState): Option[GameState] =
    handleEvent(event, gameState).orElse(
      nextHandler.flatMap(_.handle(event, gameState))
    )

  protected def handleEvent(event: Field, gameState: GameState): Option[GameState]
