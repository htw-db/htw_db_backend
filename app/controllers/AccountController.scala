package controllers

import javax.inject._
import models.{CredentialsForm, Person}
import play.api.Logger
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.{AuthService, LdapService, PersonService, PostgresService}

class AccountController @Inject()(cc: ControllerComponents, ldapService: LdapService, authService: AuthService, personService: PersonService, postgresService: PostgresService) extends AbstractController(cc) {

  implicit val personFormat: OFormat[Person] = Json.format[Person]
  val logger: Logger = Logger(this.getClass)

  def authenticate: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    logger.info(request.remoteAddress + " - " + request)
    CredentialsForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(msg => logger.error(request.remoteAddress + " - " + request + " - " + msg))
        BadRequest(errorForm.errors.head.message)
      },
      credentials => {
        val isLdapAuthenticated = ldapService.authenticate(credentials.username, credentials.password)
        if (isLdapAuthenticated) {
          val person = personService.findPersonByUsername(credentials.username)
          if (person.isEmpty) {
            val addedPerson = personService.addPerson(credentials.username)
            postgresService.createRoleInGroup(addedPerson.username)
          }
          val claim = Json.obj(("username", credentials.username))
          val token = authService.createToken(claim)
          Ok(token)
        } else {
          logger.warn(request.remoteAddress + " - " + request + " - authenticate failed")
          Unauthorized("")
        }
      })
  }
}
