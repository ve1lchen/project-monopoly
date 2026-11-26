package controller.command

import controller.GameController

class BuyPropertyCommand(controller: GameController) extends GameCommand:

  private val (playerId, field) =
    val state = controller.getState
    val player = state.getCurrentPlayer
    (player.id, state.board.get(player.position))


  override def execute(): Unit =
    controller.buyProperty(playerId, field)

  def undo(): Unit =
    controller.undoBuy(playerId, field)
