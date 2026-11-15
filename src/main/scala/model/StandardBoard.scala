package model

object StandardBoard extends MBoard(
  Vector(
    StartField(200),

    StreetField(60, RentStructure(2, 10, 30, 90, 160, 250), 50, "Mediterranean Avenue"),
    ChestField,
    StreetField(60, RentStructure(4, 20, 60, 180, 320, 450), 50, "Baltic Avenue"),
    TaxField(200, "Income Tax"),
    StationField(200, "Reading Railroad"),

    StreetField(100, RentStructure(6, 30, 90, 270, 400, 550), 50, "Oriental Avenue"),
    ChanceField,
    StreetField(100, RentStructure(6, 30, 90, 270, 400, 550), 50, "Vermont Avenue"),
    StreetField(120, RentStructure(8, 40, 100, 300, 450, 600), 50, "Connecticut Avenue"),
    JailField,

    StreetField(140, RentStructure(10, 50, 150, 450, 625, 750), 100, "St. Charles Place"),
    UtilityField(150, "Electric Company"),
    StreetField(140, RentStructure(10, 50, 150, 450, 625, 750), 100, "States Avenue"),
    StreetField(160, RentStructure(12, 60, 180, 500, 700, 900), 100, "Virginia Avenue"),
    StationField(200, "Pennsylvania Railroad"),

    StreetField(180, RentStructure(14, 70, 200, 550, 750, 950), 100, "St. James Place"),
    ChestField,
    StreetField(180, RentStructure(14, 70, 200, 550, 750, 950), 100, "Tennessee Avenue"),
    StreetField(200, RentStructure(16, 80, 220, 600, 800, 1000), 100, "New York Avenue"),
    FreeParkingField,

    StreetField(220, RentStructure(18, 90, 250, 700, 875, 1050), 150, "Kentucky Avenue"),
    ChanceField,
    StreetField(220, RentStructure(18, 90, 250, 700, 875, 1050), 150, "Indiana Avenue"),
    StreetField(240, RentStructure(20, 100, 300, 750, 925, 1100), 150, "Illinois Avenue"),
    StationField(200, "B. & O. Railroad"),

    StreetField(260, RentStructure(22, 110, 330, 800, 975, 1150), 150, "Atlantic Avenue"),
    StreetField(260, RentStructure(22, 110, 330, 800, 975, 1150), 150, "Ventnor Avenue"),
    UtilityField(150, "Water Works"),
    StreetField(280, RentStructure(24, 120, 360, 850, 1025, 1200), 150, "Marvin Gardens"),
    GoToJailField,

    StreetField(300, RentStructure(26, 130, 390, 900, 1100, 1275), 200, "Pacific Avenue"),
    StreetField(300, RentStructure(26, 130, 390, 900, 1100, 1275), 200, "North Carolina Avenue"),
    ChestField,
    StreetField(320, RentStructure(28, 150, 450, 1000, 1200, 1400), 200, "Pennsylvania Avenue"),
    StationField(200, "Short Line"),
    ChanceField,

    StreetField(350, RentStructure(35, 175, 500, 1100, 1300, 1500), 200, "Park Place"),
    TaxField(100, "Luxury Tax"),
    StreetField(400, RentStructure(50, 200, 600, 1400, 1700, 2000), 200, "Boardwalk")
  )
)
