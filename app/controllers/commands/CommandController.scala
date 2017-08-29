package controllers.commands

import java.util.Date
import javax.inject._

import models.{Car, CarService}
import play.api.mvc._

import scala.util.Try

@Singleton
class CommandController @Inject()(carService: CarService) extends Controller {

  def add = Action { request =>
    val json = request.body.asJson
    json match {
      case Some(data) =>
        val isNew = (data \ "isNew").as[Boolean]
        val mileage = Try((data \ "mileage").as[Int]).toOption
        val registrationDate = Try((data \ "registrationDate").as[Date]).toOption
        if (isNew && mileage.isDefined && registrationDate.isDefined) {
          ResetContent
        } else {
          val car = data.as[Car]
          carService.insert(car)
          Ok(views.html.index(s"record is added for id ${car.id}"))
        }
      case None => NoContent
    }
  }

  def modify(id: Int) = Action { request =>
    val json = request.body.asJson
    json match {
      case Some(data) =>
        val isNew = (data \ "isNew").as[Boolean]
        val mileage = Try((data \ "mileage").as[Int]).toOption
        val registrationDate = Try((data \ "registrationDate").as[Date]).toOption
        if (isNew && mileage.isDefined && registrationDate.isDefined) {
          ResetContent
        } else {
          val car = data.as[Car]
          carService.update(id, car)
          Ok(views.html.index(s"record is updated for id ${id}"))
        }
      case None => NoContent
    }
  }

  def delete(id: Int) = Action { request =>
    carService.delete(id)
    Ok(views.html.index(s"record is deleted for id ${id}"))
  }

}
