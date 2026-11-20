package controller.command

import controller.GameController

class RollDiceCommand(controller: GameController) extends GameCommand:
  override def execute(): Unit =
    controller.rollDice()
