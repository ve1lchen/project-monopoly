package model

import controller.command.{EndTurnCommand, GameCommand, RollDiceCommand}

class UndoManager:
  private var undoStack: List[GameCommand] = Nil
  private var redoStack: List[GameCommand] = Nil

  def doStep(command: GameCommand): Unit =
    undoStack = command::undoStack
    command.execute()
    command match
      case _: EndTurnCommand => redoStack = Nil

  def undoStep(): Unit =
    undoStack match
      case Nil =>
      case head::stack =>
        head match
          case comm: RollDiceCommand =>
            return;
          case _ =>
        head.undo()
        undoStack = stack
        redoStack = head::redoStack

  def redoStep(): Unit =
    redoStack match
      case Nil =>
      case head::stack =>
        head.redo()
        redoStack = stack
        undoStack = head::undoStack
