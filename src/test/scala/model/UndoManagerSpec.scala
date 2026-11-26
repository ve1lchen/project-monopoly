package model

import controller.GameController
import controller.command.{GameCommand, RollDiceCommand}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers:

  "An UndoManager" should:
    "execute a command when doStep is called" in:
      val undoManager = new UndoManager
      var executed = false

      val command = new GameCommand:
        override def execute(): Unit =
          executed = true
        override def undo(): Unit = {}

      undoManager.doStep(command)
      executed shouldBe true

    "undo the last executed command" in:
      val undoManager = new UndoManager
      var undone = false

      val command = new GameCommand:
        override def execute(): Unit = {}
        override def undo(): Unit =
          undone = true

      undoManager.doStep(command)
      undoManager.undoStep()

      undone shouldBe true

    "remove the command from the undo stack after undoing it" in:
      val undoManager = new UndoManager
      var undoCount = 0

      val command = new GameCommand:
        override def execute(): Unit = {}

        override def undo(): Unit =
          undoCount += 1

      undoManager.doStep(command)
      undoManager.undoStep()
      undoManager.undoStep()

      undoCount shouldBe 1

    "undo commands in reverse order" in:
      val undoManager = new UndoManager
      var undoOrder = List.empty[Int]

      val command1 = new GameCommand:
        override def execute(): Unit = {}

        override def undo(): Unit =
          undoOrder = undoOrder :+ 1

      val command2 = new GameCommand:
        override def execute(): Unit = {}

        override def undo(): Unit =
          undoOrder = undoOrder :+ 2

      undoManager.doStep(command1)
      undoManager.doStep(command2)

      undoManager.undoStep()
      undoManager.undoStep()

      undoOrder shouldBe List(2, 1)

    "do nothing when undoStep is called on an empty stack" in:
      val undoManager = new UndoManager

      noException should be thrownBy:
        undoManager.undoStep()

    "not undo a RollDiceCommand" in:
      val undoManager = new UndoManager
      var undoBool = false

      val command = new RollDiceCommand(GameController(GameState(Vector()))):
        override def execute(): Unit = {}
        override def undo(): Unit =
          undoBool = true

      undoManager.doStep(command)
      undoManager.undoStep()

      undoBool shouldBe false
