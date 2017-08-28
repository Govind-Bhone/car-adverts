package controllers.queries

import javax.inject._

import models.{Car, CarService}
import play.api._
import play.api.mvc._
import play.api.libs.json._


@Singleton
class QueryController @Inject()(carService: CarService) extends Controller {

  def showAll = Action {
    Ok(Json.toJson(carService.getAll))
  }

  def showById(id:Int)=Action{
    Ok(Json.toJson(carService.findById(id)))
  }

}