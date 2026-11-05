import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8

class MonopolySpec extends AnyWordSpec with Matchers:

  "A monopoly" should:
    "run the main method" in {
      val input = new ByteArrayInputStream("0\n".getBytes(UTF_8))

      scala.Console.withIn(input) {
        monopoly.main(Array.empty[String])
      }
    }