package model


sealed trait Field:
  def name: String
  def fieldType: FieldType

sealed trait PropertyField extends Field:
  def price: Int

case class BaseField(name: String,  fieldType: FieldType) extends Field

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

case class StreetField(name: String, fieldType: FieldType, price: Int,
  rentTable: RentStructure,
  houses: DevelopmentLevel = DevelopmentLevel.Unimproved,

  houseCost: Int,       // The cost of one house.
  hotelCost: Int) extends PropertyField {
  def getRent: Int = houses.ordinal match
    case 0 => rentTable.baseRent case 1 => rentTable.oneHouse
    case 2 => rentTable.twoHouses case 3 => rentTable.threeHouses
    case 4 => rentTable.fourHouses case 5 => rentTable.hotel

  def develop: Option[StreetField] = {
    houses.next.map(nextHouse => copy(houses = nextHouse))
  }
}

case class StationField(name: String, fieldType: FieldType, price: Int,
  // The rent depending on the amount of stations the player owns.
  rentTable: Vector[Int]) extends PropertyField {
  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  def getRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)
}

case class UtilityField(name: String, fieldType: FieldType, price: Int
                       ) extends PropertyField {
  def getRent(ownsBothUtilities: Boolean, diceRoll: Int): Int =
    diceRoll * (if ownsBothUtilities then 10 else 4)
}
