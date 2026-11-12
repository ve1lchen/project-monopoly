package model

import model.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends  AnyWordSpec with Matchers:

  "A model.Player" should {

    "start with 1500 money" in {
      val player = Player("Rene")
      player.money shouldBe 1500
    }

    "start at position 0" in {
      val player = Player("Rene")
      player.position shouldBe 0
    }

    "lose money while playing" in {
      val player = Player("Rene")
      val updated = player.pay(200)
      updated.money shouldBe 1300
    }

    "receive money while playing" in {
      val player = Player("Rene")
      val updated = player.receive(200)
      updated.money shouldBe 1700
    }

    "move while playing" in {
      val player = Player("Rene")
      val updated = player.changePosition(4)
      updated.position shouldBe 4
    }

    "not lose money for zero payment" in {
      val player = Player("Rene")
      val updated = player.pay(0)
      updated shouldBe player

    }

    "not lose money for negative payment" in {
      val player = Player("Rene")
      val updated = player.pay(-200)

      updated shouldBe player
    }

    "not receive money for negative amount" in {
      val player = Player("Rene")
      val updated = player.receive(-200)

      updated shouldBe player
    }

    "not receive money for zero receive" in {
      val player = Player("Rene")
      val updated = player.receive(0)

      updated shouldBe player
    }


  }