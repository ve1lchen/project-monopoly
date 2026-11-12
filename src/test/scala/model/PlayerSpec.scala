package model

import model.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends  AnyWordSpec with Matchers:

  "A model.Player" should {
    val player = Player("Rene")
    "start with 1500 money" in {
      player.money shouldBe 1500
    }

    "start at position 0" in {
      player.position shouldBe 0
    }

    "lose money while playing" in {
      val updated = player.pay(200)
      updated.money shouldBe 1300
      updated should not be player
    }

    "receive money while playing" in {
      val updated = player.receive(200)
      updated.money shouldBe 1700
      updated should not be player
    }

    "move while playing" in {
      val updated = player.changePosition(4)
      updated.position shouldBe 4
    }

    "not lose money for zero payment" in {
      val updated = player.pay(0)
      updated.money shouldBe 1500
      updated shouldBe player
    }

    "not receive money for zero receive" in {
      val updated = player.receive(0)
      updated.money shouldBe 1500
      updated shouldBe player
    }
  }