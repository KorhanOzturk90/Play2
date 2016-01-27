package controller

import java.util.Date

import controllers.DVLAController
import model.Vehicle
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._


@RunWith(classOf[JUnitRunner])
class DVLASpec extends Specification {
  var list = List[Vehicle]()

  "The sample car list" should {
    "return correct car" in {
      val dvla = new DVLAController
      val car1 = new Vehicle("a", "b", "", 0, new Date(), 5)
      val car2 = new Vehicle("x", "y", "", 0, new Date(), 5)
      addCarToTheList(car1)
      addCarToTheList(car2)
      val result = dvla.checkVehicleDisc(list, "a", "b")
      println(result)
      result must beSome[Vehicle]
    }
  }

  "Empty car list" should {
    "return an empty list" in {
      val dvla = new DVLAController
      dvla.checkVehicleDisc(List[Vehicle](), "a", "b") must beNone
    }
  }

  def addCarToTheList(car : Vehicle) = list = car :: list
}
