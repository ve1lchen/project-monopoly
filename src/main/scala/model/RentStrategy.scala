package model

import model.DevelopmentLevel.*

case class RentStructure(baseRent: Int,
                         oneHouse: Int, twoHouses: Int, threeHouses: Int, fourHouses: Int,
                         hotel: Int) {
  require(
    Seq(baseRent, oneHouse, twoHouses, threeHouses, fourHouses, hotel)
      .forall(_ >= 0), "Rent needs to be at least 0 or higher.")
  require(baseRent <= oneHouse && oneHouse <= twoHouses
    && twoHouses <= threeHouses && threeHouses <= fourHouses
    && fourHouses <= hotel, "The rent needs to increase.")}

enum DevelopmentLevel:
  case Undeveloped, OneHouse, TwoHouses, ThreeHouses, FourHouses, Hotel


trait RentStrategy[C]:
  def calculateRent(context: C): Option[Int]


case class StreetRentStrategy(rentTable: RentStructure)
  extends RentStrategy[DevelopmentLevel]:
  override def calculateRent(developmentLevel: DevelopmentLevel): Option[Int] =
    Some(developmentLevel match
      case Undeveloped => rentTable.baseRent
      case OneHouse => rentTable.oneHouse
      case TwoHouses => rentTable.twoHouses
      case ThreeHouses => rentTable.threeHouses
      case FourHouses => rentTable.fourHouses
      case Hotel => rentTable.hotel)

case class StationRentStrategy(rentTable: Vector[Int] = Vector(25, 50, 100, 200))
  extends RentStrategy[Int]:
  require(rentTable.nonEmpty, "The rentTable cannot be empty.")

  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  override def calculateRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)
    
case class UtilityRentStrategy(rentTable: Vector[Int] = Vector(4, 10))
  extends RentStrategy[(Int, Int)]:
  override def calculateRent(context: (Int, Int)): Option[Int] =
    val (ownedUtilitiesCount, diceRoll) = context

    require(diceRoll >= 0, "A dice roll must be at least 0.")

    rentTable.lift(ownedUtilitiesCount - 1).map(_ * diceRoll)
