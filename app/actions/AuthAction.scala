package actions

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import play.api.http.HeaderNames
import play.api.mvc._

import services.AuthService

case class UserRequest[A](username: String, request: Request[A]) extends WrappedRequest[A](request)

class AuthAction @Inject()(bodyParser: BodyParsers.Default, authService: AuthService)(implicit ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = bodyParser
  override protected def executionContext: ExecutionContext = ec

  private val headerTokenRegex = """Bearer (.+?)""".r

  override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
    val jwtToken = extractBearerToken(request).getOrElse("")
    val isValidToken = authService.isValidToken(jwtToken)
    if (isValidToken) {
      val claim = authService.decodeClaim(jwtToken)
      val username = claim.getOrElse("username", "").toString
      if (username != "") {
        block(UserRequest(username, request))
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
