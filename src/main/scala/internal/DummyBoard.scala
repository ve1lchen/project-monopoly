package internal

case class DummyBoard(
    fields: Vector[DummyField] = Vector(DummyField(100, 50, "Field1", FieldType.Street),
                          DummyField(100, 50, "Field1", FieldType.Street),
                          DummyField(100, 50, "Field2", FieldType.Street),
                          DummyField(100, 50, "Field3", FieldType.Street),
                          DummyField(100, 50, "Field4", FieldType.Street),
                          DummyField(100, 50, "Field5", FieldType.Street),
                          DummyField(100, 50, "Field6", FieldType.Street),
                          DummyField(100, 50, "Field7", FieldType.Street),
                          DummyField(100, 50, "Field8", FieldType.Street),
                          DummyField(100, 50, "Field9", FieldType.Street),
                          DummyField(100, 50, "Field10", FieldType.Street))
                     ):
  def size: Int = fields.size

  def getField(position: Int): Field =
    fields(position)
