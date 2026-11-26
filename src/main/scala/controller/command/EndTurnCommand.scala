package controller.command

import controller.GameController

class EndTurnCommand(controller: GameController) extends GameCommand:
  override def execute(): Unit =
    controller.endTurn()

  override def undo(): Unit =
    controller.undoEndTurn()
