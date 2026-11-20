package controller.command

import controller.GameController

class BuyPropertyCommand(controller: GameController) extends GameCommand:
  override def execute(): Unit =
    controller.buyProperty()