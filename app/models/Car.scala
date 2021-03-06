package models

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import anorm.SqlParser.get
import anorm.{SQL, ~}
import play.api.db.DBApi
import play.api.libs.json._
import play.api.libs.json.Json.toJson
import scala.util.Try

class Car(val id: Int,
          val title: String,
          val fuel: String,
          val price: Double,
          val mileage: Option[Int],
          val registrationDate: Option[Date])


object Car {

  implicit object CarFormat extends Format[Car] {
    // convert from JSON string to a Car object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Car] = {
      val id = (json \ "id").as[Int]
      val title = (json \ "title").as[String]
      val fuel = (json \ "fuel").as[String]
      val price = (json \ "price").as[Double]
      val mileage = Try((json \ "mileage").as[Int]).toOption
      val registrationDate = Try((json \ "registrationDate").as[Date]).toOption
      JsSuccess(new Car(id, title, fuel, price, mileage, registrationDate))
    }

    // convert from Car object to JSON (serializing to JSON)
    def writes(s: Car): JsValue = {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
      var carAsList = Seq(
        "id" -> JsNumber(s.id),
        "title" -> JsString(s.title),
        "fuel" -> JsString(s.fuel),
        "price" -> JsNumber(s.price)
      )
      if (s.registrationDate.isDefined) carAsList = carAsList.:+("registrationDate" -> JsString(dateFormat.format(s.registrationDate.get)))
      if (s.mileage.isDefined) carAsList=carAsList.:+("mileage"->JsNumber(s.mileage.get))
      JsObject(carAsList)
    }
  }

  def apply(id: Int, title: String, fuel: String,
            price: Double, mileage: Option[Int], registrationDate: Option[Date]): Car = new Car(id, title, fuel, price, mileage, registrationDate)
}

@javax.inject.Singleton
class CarService @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

  val simple = {
    get[Int]("car.id") ~
      get[String]("car.title") ~
      get[String]("car.fuel") ~
      get[Double]("car.price") ~
      get[Option[Int]]("car.mileage") ~
      get[Option[Date]]("car.registrationDate") map {
      case id ~ title ~ fuel ~ price ~ mileage ~ registrationDate =>
        new Car(id, title, fuel, price, mileage, registrationDate)
    }
  }

  def getAll: List[Car] = db.withConnection { implicit connection =>
    SQL("select * from car order by id ASC").as(simple *).
      foldLeft[List[Car]](Nil) { (cs, c) =>
      cs.:+(c)
    }
  }

  def getAllByOrderField(field: String): List[Car] = db.withConnection { implicit connection =>
    val query = s"select * from car order by $field"
    SQL(query).as(simple *).
      foldLeft[List[Car]](Nil) { (cs, c) =>
      cs.:+(c)
    }
  }

  def findById(id: Int): Option[Car] = {
    db.withConnection { implicit connection =>
      SQL("select * from car where id = {id}").on('id -> id).as(simple.singleOpt)
    }
  }

  def update(id: Int, car: Car): Int = {
    db.withConnection { implicit connection =>
      SQL(
        """
          update car
          set title = {title}, fuel = {fuel}, price = {price}, mileage = {mileage},registrationDate={registrationDate}
          where id = {id}
        """
      ).on(
        'id -> id,
        'title -> car.title,
        'fuel -> car.fuel,
        'price -> car.price,
        'mileage -> car.mileage,
        'registrationDate -> car.registrationDate
      ).executeUpdate()
    }
  }

  def insert(car: Car) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into car values (
            {id},{title}, {fuel}, {price}, {mileage},{registrationDate}
          )
        """
      ).on(
        'id -> car.id,
        'title -> car.title,
        'fuel -> car.fuel,
        'price -> car.price,
        'mileage -> car.mileage,
        'registrationDate -> car.registrationDate
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    db.withConnection { implicit connection =>
      SQL("delete from car where id = {id}").on('id -> id).executeUpdate()
    }
  }
}

