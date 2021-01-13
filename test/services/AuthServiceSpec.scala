package services

import org.scalatestplus.play.PlaySpec
import play.api.Configuration
import play.api.libs.json.Json

class AuthServiceSpec extends PlaySpec {

  "AuthServiceSpec#createToken" should {

    val mockConfig: Configuration = Configuration(("jwt.secretKey", "mockSecretKey"))
    val jwtRegex = """(.+?)\.(.+?)\.(.+?)""".r

    "create a JSON Web Token" in {
      val authService = new AuthService(mockConfig)
      val claim = Json.obj(("username", "alice"))
      val actual = authService.createToken(claim)
      actual must fullyMatch regex jwtRegex
    }

    "validate a valid JSON Web Token" in {
      val authService = new AuthService(mockConfig)
      val claim = Json.obj(("username", "alice"))
      val jsonWebToken = authService.createToken(claim)
      val actual = authService.isValidToken(jsonWebToken)
      actual mustBe true
    }

    "validate a invalid JSON Web Token" in {
      val authService = new AuthService(mockConfig)
      val actual = authService.isValidToken("")
      actual mustBe false
    }

    "get claim from valid JSON Web Token" in {
      val authService = new AuthService(mockConfig)
      val claim = Json.obj(("username", "alice"))
      val jsonWebToken = authService.createToken(claim)
      val actual = authService.decodeClaim(jsonWebToken).getOrElse(Json.obj())
      actual must equal(claim)
    }
  }
}
