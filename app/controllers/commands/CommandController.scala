package controllers.commands

import javax.inject._
import models.{Car, CarService}
import play.api.mvc._

@Singleton
class CommandController @Inject()(carService: CarService) extends Controller {

  def add = Action { request =>
    val json = request.body.asJson.get
    val car = json.as[Car]
    carService.insert(car)
    Ok(views.html.index(s"record is added for id ${car.id}"))
  }

  def modify(id: Int) = Action { request =>
    val json = request.body.asJson.get
    val car = json.as[Car]
    carService.update(id, car)
    Ok(views.html.index(s"record is updated for id ${id}"))
  }

  def delete(id: Int) = Action { request =>
    carService.delete(id)
    Ok(views.html.index(s"record is deleted for id ${id}"))
  }

}
