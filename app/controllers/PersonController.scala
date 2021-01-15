package controllers

import actions.{AuthAction, UserRequest}
import javax.inject._
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import models.Person
import services.PersonService

class PersonController @Inject()(cc: ControllerComponents, personService: PersonService, authAction: AuthAction) extends AbstractController(cc) {

  implicit val personFormat: OFormat[Person] = Json.format[Person]

  def getAll: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    val persons = personService.listPersons()
    Ok(Json.toJson(persons))
  }
}
