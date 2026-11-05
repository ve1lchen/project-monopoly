package model

import model.DevelopmentLevel.*


sealed trait Field:
  def name: String
  require(!name.isBlank, "A field-name cannot be empty.")

sealed trait PropertyField extends Field:
  def price: Int
  require(price >= 0, "The price needs to be at least 0.")


enum ActionType:
  case Start, Chance, CommunityChest, GoToJail
  case Tax (amount: Int)

case class RentStructure(
  baseRent: Int,
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

case class StandardField(
  name: String, action: Option[ActionType] = None) extends Field

case class StreetField(price: Int,
  rentTable: RentStructure, houseCost: Int, hotelCost: Int,
  name: String = "Street") extends PropertyField {
  require(houseCost > 0 && hotelCost > 0,
    "The house/hotel-cost must be higher than 0.")
  require(houseCost <= hotelCost,
    "A hotel should cost the same or more than a house")

  def getRent(developmentLevel: DevelopmentLevel): Int =
    developmentLevel match
      case Undeveloped  => rentTable.baseRent
      case OneHouse     => rentTable.oneHouse
      case TwoHouses    => rentTable.twoHouses
      case ThreeHouses  => rentTable.threeHouses
      case FourHouses   => rentTable.fourHouses
      case Hotel        => rentTable.hotel}

case class StationField(price: Int,
  // The rent depending on the amount of stations the player owns.
  rentTable: Vector[Int] = Vector(25, 50, 100, 200),
  name: String = "Station") extends PropertyField {
  require(rentTable.nonEmpty, "The rentTable cannot be empty.")
  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  def getRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)}

case class UtilityField(price: Int, name: String = "Utility",
                        both: Int = 10, one: Int = 4)
  extends PropertyField {
  def getRent(ownsBothUtilities: Boolean, diceRoll: Int): Int = {
    require(diceRoll > 0, "The dice-roll must be higher than 0.")
    diceRoll * (if ownsBothUtilities then both else one)
  }
}
