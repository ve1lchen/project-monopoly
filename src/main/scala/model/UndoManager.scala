package model

import controller.command.GameCommand

class UndoManager:
  private var undoStack: List[GameCommand] = Nil

  def doStep(command: GameCommand): Unit =
    undoStack = command::undoStack
    command.execute()

  def undoStep(): Unit =
    undoStack match
      case  Nil =>
      case head::stack =>
        head.undo()
        undoStack = stack
