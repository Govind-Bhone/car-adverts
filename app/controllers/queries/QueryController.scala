package controllers.queries

import javax.inject._

import models.CarService
import play.api.mvc._
import play.api.libs.json._
import net.liftweb.json.DefaultFormats

@Singleton
class QueryController @Inject()(carService: CarService) extends Controller {
  implicit val formats = DefaultFormats
  def showAll = Action {
    val data = carService.getAll
    if (data.isEmpty) Ok(views.html.index(s"No records found")) else
      Ok(Json.toJson(data))
  }

  def showAllByOrder(field: String) = Action {
    Ok(Json.toJson(carService.getAllByOrderField(field)))
  }

  def showById(id: Int) = Action {
    carService.findById(id) match {
      case Some(record)=>Ok(Json.toJson(record))
      case None=>Ok(views.html.index(s"No records found for id ${id}"))
    }
  }
}
