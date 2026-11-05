package model

import model.{Field, FieldType}

case class DummyField (
  cost: Int,
  rent: Int,
  name: String,
  fieldType: FieldType
                      ) extends Field
  