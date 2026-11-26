package controller

import controller.GameController
import controller.command.{BuyPropertyCommand, RollDiceCommand}
import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameControllerSpec extends AnyWordSpec with Matchers:

  "A controller.GameController" should:
    "initialize with correct players" in:
      val players: Vector[Player] = Vector(Player("Alice"), Player("Renee"))
      val controller = GameController(GameState(players), new RandomDice)

      controller.getState.players shouldBe players

    "manage turn" in:
      val players: Vector[Player] = Vector(Player("Alice"), Player("Renee"))
      val controller = GameController(GameState(players), new FixedDice((3, 4)))
      RollDiceCommand(controller).execute()
      controller.endTurn()
      val updatedState = controller.getState
      updatedState.currentPlayerIndex shouldBe 1
      updatedState
        .players(updatedState.currentPlayerIndex - 1)
        .position shouldBe 7

    "manage turn with double" in:
      val players: Vector[Player] = Vector(Player("Alice"), Player("Renee"))
      val controller = GameController(GameState(players), new FixedDice((3, 3)))
      RollDiceCommand(controller).execute()
      controller.endTurn()
      val updatedState = controller.getState
      updatedState.currentPlayerIndex shouldBe 0
      updatedState.players(updatedState.currentPlayerIndex).position shouldBe 6

    "manage turn with START crossing" in:
      val players: Vector[Player] =
        Vector(Player("Alice", position = 40), Player("Renee"))
      val controller = GameController(GameState(players), new FixedDice((2, 3)))
      RollDiceCommand(controller).execute()
      controller.endTurn()
      val updatedState = controller.getState
      updatedState.players(0).position shouldBe updatedState
        .board.relativePosition(40 + 2 + 3)

    "be able to buy property" in:
      val players: Vector[Player] =
        Vector(Player("Alice", position = 3, id = 0), Player("Renee", id = 1))
      val controller = GameController(GameState(players), new FixedDice((2, 1)))
      BuyPropertyCommand(controller).execute()

      val state = controller.getState
      state.propertyMap should contain(3 -> 0)
      state.players(1).money shouldBe 1500
      state.players(0).money shouldBe 1500 - StandardBoard.get(3)
        .asInstanceOf[PropertyField].price

    "not be able to buy property with insufficient money" in:
      val players: Vector[Player] =
        Vector(Player("Alice", money = 0, position = 3), Player("Renee"))
      val initialState = GameState(players)
      val controller   = GameController(initialState, new FixedDice((2, 1)))
      BuyPropertyCommand(controller).execute()

      controller.getState shouldBe initialState

    "not be able to buy property with an owner" in:
      val players: Vector[Player] =
        Vector(Player("Alice", position = 3, id = 0), Player("Renee", id = 1))
      val initialState = GameState(players, propertyMap = Map(3 -> 1))
      val controller   = GameController(initialState, new FixedDice((2, 1)))
      BuyPropertyCommand(controller).execute()

      controller.getState shouldBe initialState

    "not be able to buy Start field" in:
      val players: Vector[Player] = Vector(Player("Alice"), Player("Renee"))
      val initialState            = GameState(players)
      val controller = GameController(initialState, new FixedDice((2, 1)))
      BuyPropertyCommand(controller).execute()

      controller.getState shouldBe initialState

    "distinguish own property from another player's property" in:
      val players = Vector(
        Player("Alice", position = 3, id = 0),
        Player("Renee", id = 1)
      )

      val ownController = GameController(
        GameState(players, propertyMap = Map(3 -> 0)),
        new FixedDice((2, 1))
      )

      ownController.buyProperty(players.head,
        ownController.getState.board.get(
          players.head.position)) shouldBe model.ResultStatus.OwnerIsYou

      val otherController = GameController(
        GameState(players, propertyMap = Map(3 -> 1)),
        new FixedDice((2, 1))
      )

      otherController.buyProperty(players.head,
        otherController.getState.board.get(
          players.head.position)
      ) shouldBe model.ResultStatus.AlreadyOwned

      otherController.getCurrentFieldOwner shouldBe Some(players(1))

    "allow buying property with exactly enough money" in:
      val position = 3
      val field = StandardBoard.get(position).asInstanceOf[PropertyField]
      val players = Vector(
        Player("Alice", money = field.price, position = position, id = 0),
        Player("Renee", id = 1)
      )

      val controller = GameController(
        GameState(players),
        new FixedDice((2, 1))
      )

      controller.buyProperty(players.head, field) shouldBe model.ResultStatus.Success
      controller.getState.players(0).money shouldBe 0
      controller.getState.propertyMap should contain(3 -> 0)

    "not grant an extra turn when lastRoll is missing" in:
      val players = Vector(
        Player("Alice", id = 0),
        Player("Renee", id = 1)
      )

      val initialState = GameState(
        players,
        lastRoll = None,
        turnState = model.AwaitingActionState
      )

      val controller = GameController(
        initialState,
        new FixedDice((3, 3))
      )

      controller.endTurn()

      controller.getState.currentPlayerIndex shouldBe 1
