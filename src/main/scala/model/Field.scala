package model

import model.DevelopmentLevel.*


sealed trait Field:
  def name: String = "Field"
  require(!name.isBlank, "A field-name cannot be empty.")

sealed trait EventField extends Field:
  def name: String = "Event"

case class StartField(name: String = "Start") extends EventField
case class ChestField(name: String = "Community Chest") extends EventField
case class ChanceField(name: String = "Chance") extends EventField
case class GoToJailField(name: String = "Go To Jail") extends EventField

// A field which gives/takes a constant number of money.
sealed trait MoneyField extends EventField:
  def money: Int

case class TaxField(money: Int, override val name: String = "Tax")
  extends MoneyField:
  require(money > 0, "Tax must be higher than 0")


sealed trait EventlessField extends Field

case class JailField(name: String = "Jail") extends EventlessField
case class FreeParkingField(name: String = "Free Parking") extends EventlessField


sealed trait PropertyField extends Field:
  def price: Int
  require(price >= 0, "The price needs to be at least 0.")

sealed trait RentField[C] extends Field:
  def getRent(context: C): Int

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


case class StreetField(price: Int,
  rentTable: RentStructure, houseCost: Int)
                      (val hotelCost: Int = houseCost,
                       val name: String = "Street")
  extends PropertyField, RentField[DevelopmentLevel] {
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
  name: String = "Station") extends PropertyField, RentField[Int] {
  require(rentTable.nonEmpty, "The rentTable cannot be empty.")
  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  def getRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)}


case class UtilityField(price: Int, name: String = "Utility",
                        both: Int = 10, one: Int = 4)
  extends PropertyField, RentField[(Boolean, Int)] {
  def getRent(ownsBothUtilities: Boolean, diceRoll: Int): Int = {
    require(diceRoll > 0, "The dice-roll must be higher than 0.")
    diceRoll * (if ownsBothUtilities then both else one)
  }
}
