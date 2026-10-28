package internal

class RandomDice extends Dice:
  def roll(): (Int, Int) =
    (scala.util.Random.between(1, 7), scala.util.Random.between(1, 7))

class FixedDice(initValues: (Int, Int)) extends Dice:
  private var values = initValues

  def setValues(newValues: (Int, Int)): Unit =
    require(newValues._1 >= 1 && newValues._1 <= 6)
    require(newValues._2 >= 1 && newValues._2 <= 6)

    values = newValues


  def roll(): (Int, Int) =
    values
    
   
  