package controllers.queries

import javax.inject._

import models.Car
import play.api._
import play.api.mvc._
import play.api.libs.json._


@Singleton
class QueryController @Inject() extends Controller {

  def showAll = Action {
    val cars=Car.allCars
    Ok(Json.toJson(cars))
  }

  def showById(id:Int)=Action{
    val car=Car.findById(id)
    Ok(Json.toJson(car))
  }

}