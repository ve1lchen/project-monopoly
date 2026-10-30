package model

enum FieldType:
  case Street, Station, Utility // Properties
  case Start, Chance, CommunityChest, Tax, GoToJail // Triggers an action
  case Parking, Jail

  def isBuyable: Boolean = this match
    case Street | Station | Utility => true
    case _                          => false

  def triggersAction: Boolean = this match
    case Start | Chance | CommunityChest | Tax | GoToJail => true
    case _                                                => false