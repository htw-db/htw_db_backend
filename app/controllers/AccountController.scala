package controllers

import javax.inject._
import models.{CredentialsForm, Person}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.{AuthService, LdapService, PersonService}

class AccountController @Inject()(cc: ControllerComponents, ldapService: LdapService, authService: AuthService, personService: PersonService) extends AbstractController(cc) {

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
            personService.addPerson(credentials.username)
          }
          val claim = Json.obj(("username", credentials.username))
          val token = authService.createToken(claim)
          Ok(token)
        } else {
          Unauthorized("")
        }
      })
  }
}
