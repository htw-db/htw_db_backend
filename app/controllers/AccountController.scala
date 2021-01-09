package controllers

import javax.inject._
import models.Person
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import services.LdapService

class AccountController @Inject()(cc: ControllerComponents, ldapService: LdapService) extends AbstractController(cc) {

  implicit val personFormat: OFormat[Person] = Json.format[Person]

  def authenticate: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val result = ldapService.authenticate("", "")
    Ok("ok")
  }
}
