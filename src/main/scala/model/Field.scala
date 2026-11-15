package model

import model.DevelopmentLevel.*


sealed trait Field:
  def name: String = "Field"
  require(!name.isBlank, "A field-name cannot be empty.")

/**
 * A field where something happens.
 */
sealed trait EventField(val name: String = "Event") extends Field

sealed trait OnEnterField(val name: String = "Enter-Event") extends EventField
sealed trait OnPassField(val name: String = "Pass-Event") extends EventField

case object ChestField extends OnEnterField("Community Chest")
case object ChanceField extends OnEnterField("Chance")
case object GoToJailField extends OnEnterField("Go To Jail")

/**
 * A field which gives/takes a (constant) number of money.
 */
sealed trait MoneyField[C](val name: String = "Money") extends EventField:
  def money(context: C): Option[Int]

sealed trait GainMoneyField(val name: String = "Gain Money") extends MoneyField[Unit]:
  def amount: Int
  final override def money(context: Unit): Option[Int] = Some(amount)
  require(amount > 0, "Money gained must be more than 0.")
  
sealed trait LoseMoneyField(val name: String = "Lose Money") extends MoneyField[Unit]:
  def amount: Int
  final override def money(context: Unit): Option[Int] = Some(-amount)
  require(amount > 0, "Money lost must be more than 0.")

case class StartField(amount: Int, override val name: String = "Start")
  extends OnPassField, GainMoneyField

case class TaxField(amount: Int, override val name: String = "Tax")
  extends OnEnterField, LoseMoneyField


/**
 * A field where nothing happens.
 */
sealed trait EventlessField(val name: String = "Normal Field") extends Field

case object JailField extends EventlessField("Jail")
case object FreeParkingField extends EventlessField("Free Parking")

/**
 * A field where the player can do something.
 */
sealed trait ActionField(val name: String = "Action") extends Field

/**
 * A field which the player can buy.
 */
sealed trait PropertyField(val name: String = "Property") extends ActionField:
  def price: Int
  require(price >= 0, "The price needs to be at least 0.")

/**
 * A field where a player needs to pay rent.
 * @tparam C The context required.
 */
sealed trait RentField[C](val name: String = "Rent") extends MoneyField[C]:
  protected def calculateRent(context: C): Option[Int]
  final override def money(context: C): Option[Int] = calculateRent(context).map(-_)

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


case class StreetField(price: Int,
  rentTable: RentStructure, houseCost: Int, name: String = "Street", hotelCost: Int)
  extends PropertyField, RentField[DevelopmentLevel]:
  require(houseCost > 0 && hotelCost > 0,
    "The house/hotel-cost must be higher than 0.")
  require(houseCost <= hotelCost,
    "A hotel should cost the same or more than a house")

  protected def calculateRent(developmentLevel: DevelopmentLevel): Option[Int] =
    Some(developmentLevel match
      case Undeveloped  => rentTable.baseRent
      case OneHouse     => rentTable.oneHouse
      case TwoHouses    => rentTable.twoHouses
      case ThreeHouses  => rentTable.threeHouses
      case FourHouses   => rentTable.fourHouses
      case Hotel        => rentTable.hotel)

object StreetField:
  def apply(price: Int, rentTable: RentStructure,
            houseCost: Int, name: String): StreetField =
    new StreetField(price, rentTable, houseCost, name, houseCost)


case class StationField(price: Int, name: String = "Station",
  // The rent depending on the amount of stations the player owns.
  rentTable: Vector[Int] = Vector(25, 50, 100, 200)
                       ) extends PropertyField, RentField[Int]:
  require(rentTable.nonEmpty, "The rentTable cannot be empty.")
  /**
   * @param ownedStationsCount the total amount of stations this player owns
   * @return the rent or None
   */
  protected def calculateRent(ownedStationsCount: Int): Option[Int] =
    rentTable.lift(ownedStationsCount - 1)


case class UtilityField(price: Int, name: String = "Utility",
                        rentTable: Vector[Int] = Vector(4, 10))
  extends PropertyField, RentField[(Int, Int)]:
  
  def calculateRent(context: (Int, Int)): Option[Int] = {
    val (ownedUtilitiesCount, diceRoll) = context
    
    require(diceRoll > 0, "The dice-roll must be higher than 0.")
    
    Option(rentTable(ownedUtilitiesCount - 1) * diceRoll)
  }
