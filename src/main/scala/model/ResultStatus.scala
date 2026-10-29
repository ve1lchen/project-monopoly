package model

enum ResultStatus:
  case Success
  case NotPurchasable
  case OwnerIsYou
  case AlreadyOwned
  case InsufficientFunds

