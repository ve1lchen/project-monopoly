package model

import model.*

case class RentContext(
  ownedStationsCount: Int = 0,
  ownedUtilitiesCount: Int = 0):

  /**
   * @return a map of PlayerId to RentContext
   */
  def createMap(state: GameState): Map[Int, RentContext] =
    state.propertyMap.foldLeft(Map.empty[Int, RentContext]):
      case (acc, (fieldId, playerId)) =>
        val current = acc.getOrElse(playerId, RentContext())
        state.board.get(fieldId) match
          case _: StationField =>
            acc.updated(playerId, current.copy(
              ownedStationsCount = current.ownedStationsCount + 1))

          case _: UtilityField =>
            acc.updated(playerId, current.copy(
              ownedUtilitiesCount = current.ownedUtilitiesCount + 1))

          case _ => acc
          
trait RentFunctions:
  def developmentLevel: DevelopmentLevel
  def diceRoll: Int

case class DefaultRentFunctions(getLevel: () => DevelopmentLevel,
                                 rollDice: () => Int) extends RentFunctions:

  override def developmentLevel: DevelopmentLevel =
    getLevel()

  override def diceRoll: Int =
    rollDice()



case class MoneyCalculator(rentFunctions: RentFunctions):
  def calculateMoney(
    field: RentField[?],
    context: RentContext): Option[Int] =
    field.money(context, rentFunctions)
