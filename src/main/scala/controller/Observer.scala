package controller

trait Observer:
  def update(): Unit
  
trait Observable:
  private var observers: Vector[Observer] = Vector.empty
  
  def addObserver(observer: Observer): Unit =
    observers = observers :+ observer

  protected def notifyObservers(): Unit =
    observers.foreach(_.update())
