package controllers

import javax.inject._
import models.{CredentialsForm, CredentialsFormData, Person}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.{LdapService, PersonService}

class AccountController @Inject()(cc: ControllerComponents, ldapService: LdapService, personService: PersonService) extends AbstractController(cc) {

  implicit val personFormat: OFormat[Person] = Json.format[Person]

  def authenticate: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    CredentialsForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        BadRequest(errorForm.errors.head.message)
      },
      credentials => {
        val isLdapAuthenticated = ldapService.authenticate(credentials.username, credentials.password)
        if (isLdapAuthenticated) {
          val person = personService.findPersonByUsername(credentials.username)
          if (person.isEmpty) {
            val addedPerson = personService.addPerson(credentials.username)
          }
          Ok("ok")
        } else {
          Unauthorized("")
        }
      })
  }
}
