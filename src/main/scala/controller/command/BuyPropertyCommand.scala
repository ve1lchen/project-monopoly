package controller.command

import controller.GameController

class BuyPropertyCommand(controller: GameController) extends GameCommand:

  private val (playerIndex, field) =
    val state = controller.getState
    val player = state.getCurrentPlayer
    (state.players.indexOf(player), state.board.get(player.position))


  override def execute(): Unit =
    controller.buyProperty(playerIndex, field)

  def undo(): Unit =
    controller.undoBuy(playerIndex, field)
