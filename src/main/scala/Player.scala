case class Player(
    name: String,
    money: Int = 1500,
    position: Int = 0,
    inJail: Boolean = false,
    onVacation: Boolean = false
):
  def pay(amount: Int): Player =
    if amount > 0 then
      copy(money = money - amount)
    else
      this

  def receive(amount: Int): Player =
    if amount > 0 then
      copy(money = money + amount)
    else
      this

  def move(steps: Int): Player =
    copy(position = position + steps)
