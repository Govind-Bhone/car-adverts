package models

import java.sql.Date
import java.text.SimpleDateFormat
import play.api.libs.json._
import play.api.libs.json.Json.toJson


class Car (val id:Int,
           val title:String,
           val fuel:String,
           val price:Double,
           val mileage:Int,
           val registrationDate:Date)

object Car{
  implicit object CarFormat extends Format[Car] {
    // convert from JSON string to a Car object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Car] = {
      val id = (json \ "id").as[Int]
      val title = (json \ "title").as[String]
      val fuel = (json \ "fuel").as[String]
      val price = (json \ "price").as[Double]
      val mileage = (json \ "mileage").as[Int]
      val registrationDate = (json \ "registrationDate").as[Date]
      JsSuccess(new Car(id, title, fuel, price, mileage, registrationDate))
    }

    // convert from Car object to JSON (serializing to JSON)
    def writes(s: Car): JsValue = {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
      val carAsList = Seq(
        "id" -> JsNumber(s.id),
        "title" -> JsString(s.title),
        "fuel" -> JsString(s.fuel),
        "price" -> JsNumber(s.price),
        "mileage" -> JsNumber(s.mileage),
        "registrationDate" -> toJson(
          Some(s.registrationDate).map(
            date => dateFormat.format(date)
          ).getOrElse(
            ""
          )
        )
      )
      JsObject(carAsList)
    }
  }

  private var cars=List[Car](
    Car(101,"Mercedes","diesel",45000,20,new java.sql.Date(System.currentTimeMillis())),
    Car(102,"Audi","diesel",45000,20,new java.sql.Date(System.currentTimeMillis())),
    Car(103,"Tata","diesel",45000,20,new java.sql.Date(System.currentTimeMillis())),
    Car(104,"Volgswagen","diesel",45000,20,new java.sql.Date(System.currentTimeMillis()))
  )

  def apply(id:Int,title:String,fuel:String,
            price:Double,mileage:Int,registrationDate:Date):Car=new Car(id,title,fuel,price,mileage,registrationDate)

  def allCars=cars
  def findById(id:Int)=cars.filter(_.id==id)
  def add(car:Car)=cars=cars.+:(car)
  def remove(car:Car)=cars=cars.filter(_.id!=car.id)
  def removeById(id:Int)=cars=cars.filter(_.id!=id)
}

