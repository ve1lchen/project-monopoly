trait Board:
  val size: Int
  val fieldsVector: Vector[Field]
  
  def goto(target: Int): Player // not random goto
  
  



