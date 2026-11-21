package model


trait Field:
  def name: String = "Field"

  protected def validateName(name: String): Unit =
    require(name != null && !name.isBlank,
      "A field-name cannot be empty.")

/**
 * A field where something happens.
 */
trait EventField extends Field:
  override def name: String = "Event"

trait OnEnterField extends EventField:
  override def name: String = "Enter-Event"
trait OnPassField extends EventField:
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
trait MoneyField[C] extends EventField:
  override def name = "Money"
  def money(context: C): Option[Int]
  validateName(name)

trait GainMoneyField extends MoneyField[Unit]:
  override def name = "Gain Money"
  def amount: Int
  final override def money(context: Unit): Option[Int] = Some(amount)
  require(amount > 0, "Money gained must be more than 0.")
  
trait LoseMoneyField extends MoneyField[Unit]:
  override def name = "Lose Money"
  def amount: Int
  final override def money(context: Unit): Option[Int] = Some(-amount)
  require(amount > 0, "Money lost must be more than 0.")

case class StartField(amount: Int, override val name: String = "Start")
  extends OnPassField, OnEnterField, GainMoneyField

case class TaxField(amount: Int, override val name: String = "Tax")
  extends OnEnterField, LoseMoneyField


/**
 * A field where nothing happens.
 */
trait EventlessField(override val name: String = "Normal Field") extends Field

case object JailField extends EventlessField("Jail")
case object FreeParkingField extends EventlessField("Free Parking")

/**
 * A field where the player can do something.
 */
trait ActionField() extends Field:
  override def name: String = "Action"

/**
 * A field which the player can buy.
 */
trait PropertyField extends ActionField:
  override def name: String = "Property"
  def price: Int
  protected def validatePrice(price: Int): Unit =
    require(price >= 0, "The price needs to be at least 0.")

/**
 * A field where a player needs to pay rent.
 * @tparam C The context required.
 */
trait RentField[C] extends MoneyField[C]:
  override def name = "Rent"
  val rentStrategy: RentStrategy[C]
  final override def money(context: C): Option[Int] =
    rentStrategy.calculateRent(context).map(-_)


case class StreetField(price: Int,
  rentStrategy: RentStrategy[DevelopmentLevel], houseCost: Int,
  override val name: String = "Street", hotelCost: Int)
  extends PropertyField, RentField[DevelopmentLevel]:
  validatePrice(price)
  require(houseCost > 0 && hotelCost > 0,
    "The house/hotel-cost must be higher than 0.")
  require(houseCost <= hotelCost,
    "A hotel should cost the same or more than a house")

object StreetField:
  def apply(price: Int, rentStrategy: RentStrategy[DevelopmentLevel],
            houseCost: Int, name: String): StreetField =
    new StreetField(price, rentStrategy, houseCost, name, houseCost)

  def apply(price: Int, rentTable: RentStructure,
            houseCost: Int, name: String, hotelCost: Int): StreetField =
    new StreetField(price, StreetRentStrategy(rentTable), houseCost,
      name, hotelCost)
  
  def apply(price: Int, rentTable: RentStructure,
            houseCost: Int, name: String): StreetField =
    new StreetField(price, StreetRentStrategy(rentTable), houseCost,
      name, houseCost)


case class StationField(price: Int, override val name: String = "Station",
  rentStrategy: RentStrategy[Int]) extends PropertyField, RentField[Int]:
  validatePrice(price)

object StationField:
  def apply(price: Int, name: String, rentTable: Vector[Int]): StationField =
    new StationField(price, name, StationRentStrategy(rentTable))

  def apply(price: Int, name: String): StationField =
    new StationField(price, name, StationRentStrategy())


case class UtilityField(price: Int, override val name: String = "Utility",
                        rentStrategy: RentStrategy[(Int, Int)])
  extends PropertyField, RentField[(Int, Int)]:
  validatePrice(price)

object UtilityField:
  def apply(price: Int, name: String, rentTable: Vector[Int]): UtilityField =
    new UtilityField(price, name, UtilityRentStrategy(rentTable))

  def apply(price: Int, name: String): UtilityField =
    new UtilityField(price, name, UtilityRentStrategy())
