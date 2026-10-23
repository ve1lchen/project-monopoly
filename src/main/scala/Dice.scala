object Dice:
  def roll(): (Int, Int) =
    (scala.util.Random.between(1, 7), scala.util.Random.between(1, 7))
