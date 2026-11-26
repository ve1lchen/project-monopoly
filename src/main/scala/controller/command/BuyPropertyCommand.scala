package controller.command

import controller.GameController
import model.{Field, Player}

class BuyPropertyCommand(controller: GameController) extends GameCommand:

  private val player: Player = controller.getState.getCurrentPlayer
  private val field: Field = controller.getState.board.get(player.position)


  override def execute(): Unit =
    controller.buyProperty(player, field)

  def undo(): Unit =
    controller.undoBuy(player, field)
