package model

import model.{DummyField, Field, FieldType}

case class DummyBoard(
    fields: Vector[DummyField] = Vector(DummyField(0, 0, "Start", FieldType.Start),
                          DummyField(100, 50, "Field1", FieldType.Business),
                          DummyField(100, 50, "Field2", FieldType.Business),
                          DummyField(100, 50, "Field3", FieldType.Business),
                          DummyField(100, 50, "Field4", FieldType.Business),
                          DummyField(100, 50, "Field5", FieldType.Business),
                          DummyField(100, 50, "Field6", FieldType.Business),
                          DummyField(100, 50, "Field7", FieldType.Business),
                          DummyField(100, 50, "Field8", FieldType.Business),
                          DummyField(100, 50, "Field9", FieldType.Business),
                          DummyField(100, 50, "Field10", FieldType.Business))
                     ):
  def size: Int = fields.size

  def getField(position: Int): DummyField =
    fields(position)
