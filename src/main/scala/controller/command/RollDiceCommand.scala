package controller.command

import controller.GameController

class RollDiceCommand(controller: GameController) extends GameCommand:
  
  lazy val diceRoll: (Int, Int) = controller.dice.roll()
  
  override def execute(): Unit =
    controller.rollDice(diceRoll)

  override def undo(): Unit =
    controller.undoRollDice(diceRoll)
