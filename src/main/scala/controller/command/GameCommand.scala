package controller.command

trait GameCommand:
  def execute(): Unit
  def undo(): Unit
