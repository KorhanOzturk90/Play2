package controllers

import java.text.SimpleDateFormat

import model.Vehicle
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.mvc._

import scala.collection.mutable

class DVLAController extends Controller() {

  val dvlaForm: Form[SearchDVLAForm] = Form {
    mapping(
      "Plate Number" -> nonEmptyText,
      "Make" -> nonEmptyText
    )(SearchDVLAForm.apply)(SearchDVLAForm.unapply)
  }

  def index = Action {
    Ok(views.html.search(dvlaForm))
  }

  def result(plate: String, make : String) = Action {
    val list = mutable.MutableList[Vehicle]()
    scala.io.Source.fromFile("vehicles.txt").getLines().foreach{
      l => val Array(plateNo, make, color, engineSize, registrationDate, cylinder) = l.split(" ")
        list += Vehicle(plateNo, make, color, engineSize.toInt,
          new SimpleDateFormat("yyyy-MM-dd").parse(registrationDate), cylinder.toInt)
    }
    checkVehicleDisc(list.toList, plate, make) match {
      case Some(x) => Ok(views.html.result(x))
      case _ => NotFound("Could not find any vehicle with specified plate number and make")
    }
  }

  def findVehicleInformation = Action { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    dvlaForm.bindFromRequest.fold(
      errorForm => {
        NotFound
      },
      formVehicle => {
        Redirect(routes.DVLAController.result(formVehicle.plateNo, formVehicle.make ))
      }
    )
  }

  def checkVehicleDisc(carList: List[Vehicle], plateNo: String, make: String): Option[Vehicle] =
    carList find (c => (c.plateNo.equalsIgnoreCase(plateNo) && c.make.equalsIgnoreCase(make)))

}

case class SearchDVLAForm(plateNo: String, make: String)
