package controllers.commands

import javax.inject._

import models.Car
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CommandController @Inject() extends Controller {

  def add =Action { request =>
    val json = request.body.asJson.get
    val car = json.as[Car]
    Car.add(car)
    Ok(views.html.add(s"record is added for id ${car.id}"))
  }
  def modify(id:Int)=Action {request =>
    val json = request.body.asJson.get
    val car = json.as[Car]
    Car.removeById(id)
    Car.add(car)
    Ok(views.html.update(s"record is updated for id ${id}"))
  }
  def delete(id:Int) =Action{request =>
    Car.removeById(id)
    Ok(views.html.delete(s"record is deleted for id ${id}"))
  }

}
