package model

import model.DevelopmentLevel.*


sealed trait Field:
  def name: String = "Field"

  protected def validateName(name: String): Unit =
    require(name != null && !name.isBlank,
      "A field-name cannot be empty.")

/**
 * A field where something happens.
 */
sealed trait EventField extends Field:
  override def name: String = "Event"

sealed trait OnEnterField extends EventField:
  override def name: String = "Enter-Event"
sealed trait OnPassField extends EventField:
  override def name: String = "Pass-Event"

case object ChestField extends OnEnterField:
  override val name = "Community Chest"
case object ChanceField extends OnEnterField:
  override val name = "Chance"
case object GoToJailField extends OnEnterField:
  override val name = "Go To Jail"

/**
 * A field which gives/takes a (constant) number of money.
 */
sealed trait MoneyField[C] extends EventField:
  override def name = "Money"
  def money(context: C): Option[Int]
  validateName(name)

sealed trait GainMoneyField extends MoneyField[Unit]:
  override def name = "Gain Money"
  def amount: Int
  final override def money(context: Unit): Option[Int] = Some(amount)
  require(amount > 0, "Money gained must be more than 0.")
  
sealed trait LoseMoneyField extends MoneyField[Unit]:
  override def name = "Lose Money"
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
sealed trait EventlessField(override val name: String = "Normal Field") extends Field

case object JailField extends EventlessField("Jail")
case object FreeParkingField extends EventlessField("Free Parking")

/**
 * A field where the player can do something.
 */
sealed trait ActionField() extends Field:
  override def name: String = "Action"

/**
 * A field which the player can buy.
 */
sealed trait PropertyField extends ActionField:
  override def name: String = "Property"
  def price: Int
  require(price >= 0, "The price needs to be at least 0.")

/**
 * A field where a player needs to pay rent.
 * @tparam C The context required.
 */
sealed trait RentField[C] extends MoneyField[C]:
  override def name = "Rent"
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
  rentTable: RentStructure, houseCost: Int, override val name: String = "Street", hotelCost: Int)
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


case class StationField(price: Int, override val name: String = "Station",
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


case class UtilityField(price: Int, override val name: String = "Utility",
                        rentTable: Vector[Int] = Vector(4, 10))
  extends PropertyField, RentField[(Int, Int)]:
  
  def calculateRent(context: (Int, Int)): Option[Int] = {
    val (ownedUtilitiesCount, diceRoll) = context
    
    require(diceRoll > 0, "The dice-roll must be higher than 0.")
    
    Option(rentTable(ownedUtilitiesCount - 1) * diceRoll)
  }
