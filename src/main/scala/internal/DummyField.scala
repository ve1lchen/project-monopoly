package internal

case class DummyField (
  val cost: Int,
  rent: Int,
  name: String,
  fieldType: FieldType
                      ) extends Field
  