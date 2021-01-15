package actions

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import play.api.http.HeaderNames
import play.api.mvc._

import models.Person
import services.{AuthService, PersonService}

case class UserRequest[A](person: Person, request: Request[A]) extends WrappedRequest[A](request)

class AuthAction @Inject()(bodyParser: BodyParsers.Default, authService: AuthService, personService: PersonService)(implicit ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = bodyParser

  override protected def executionContext: ExecutionContext = ec

  private val headerTokenRegex = """Bearer (.+?)""".r

  override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
    val jwtToken = extractBearerToken(request).getOrElse("")
    val isValidToken = authService.isValidToken(jwtToken)
    if (isValidToken) {
      val claim = authService.decodeClaim(jwtToken)
      if (claim.isDefined) {
        val claimMap = claim.get.value
        val username = claimMap.getOrElse("username", "").toString.replace("\"", "")
        val person = personService.findPersonByUsername(username)
        if (person.isDefined) {
          print(person.get)
          block(UserRequest(person.get, request))
        } else {
          Future.successful(Results.Unauthorized)
        }
      } else {
        Future.successful(Results.Unauthorized)
      }
    } else {
      Future.successful(Results.Unauthorized)
    }
  }

  private def extractBearerToken[A](request: Request[A]): Option[String] =
    request.headers.get(HeaderNames.AUTHORIZATION) collect {
      case headerTokenRegex(token) => token
    }

}
