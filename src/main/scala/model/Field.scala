package model

import model.DevelopmentLevel.*
import model.FieldType.{Station, Street, Utility}


sealed trait Field:
  def name: String
  def fieldType: FieldType

sealed trait PropertyField extends Field:
  def price: Int

case class BaseField(fieldType: FieldType, name: String = "Field") extends Field

case class RentStructure(
  baseRent: Int,
  oneHouse: Int, twoHouses: Int, threeHouses: Int, fourHouses: Int,
  hotel: Int)

enum DevelopmentLevel:
  case Unimproved
  case OneHouse, TwoHouses, ThreeHouses, FourHouses
  case Hotel

  def next: Option[DevelopmentLevel] =
    if this == Hotel then None
    else Some(DevelopmentLevel.fromOrdinal(ordinal + 1))

case class StreetField(price: Int,
  rentTable: RentStructure,
  houses: DevelopmentLevel = Unimproved,

  houseCost: Int,       // The cost of one house.
  hotelCost: Int,

  name: String = "Street", fieldType: FieldType = Street
                      ) extends PropertyField {
  def getRent: Int = houses match
    case Unimproved => rentTable.baseRent case OneHouse => rentTable.oneHouse
    case TwoHouses => rentTable.twoHouses case ThreeHouses => rentTable.threeHouses
    case FourHouses => rentTable.fourHouses case Hotel => rentTable.hotel

  def develop: Option[StreetField] = {
    houses.next.map(nextHouse => copy(houses = nextHouse))
  }
}

case class StationField(price: Int,
  // The rent depending on the amount of stations the player owns.
  rentTable: Vector[Int],
  name: String = "Station", fieldType: FieldType = Station
                       ) extends PropertyField {
  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  def getRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)
}

case class UtilityField(
  price: Int,
  name: String = "Utility", fieldType: FieldType = Utility
                       ) extends PropertyField {
  def getRent(ownsBothUtilities: Boolean, diceRoll: Int): Int =
    diceRoll * (if ownsBothUtilities then 10 else 4)
}
