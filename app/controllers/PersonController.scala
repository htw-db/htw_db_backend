package controllers

import javax.inject._
import models.Person
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import services.PersonService

class PersonController @Inject()(cc: ControllerComponents, personService: PersonService) extends AbstractController(cc) {

  implicit val personFormat: OFormat[Person] = Json.format[Person]

  def getAll: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val persons = Await.result(personService.listPersons(), Duration.Inf)
    Ok(Json.toJson(persons))
  }
}
