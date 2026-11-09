package model

case class DummyBoard(
   fields: Vector[Field] = Vector(
     StandardField("Start", Some(ActionType.Start)),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field1"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field2"
     ),
       StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field3"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field4"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field5"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field6"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field7"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field8"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field9"
     ),
     StreetField(
       price = 100,
       rentTable = RentStructure(50, 60, 70, 80, 90, 100),
       houseCost = 50,
       hotelCost = 50,
       name = "Field10"
     ),
   )
                     ) extends Board:

  override def size: Int = fields.size

  override def getField(position: Int): Option[Field] =
    fields.lift(position)