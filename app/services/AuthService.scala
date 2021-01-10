package services

import com.google.inject.Inject
import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.Configuration
import play.api.libs.json.JsObject


class AuthService @Inject()(config: Configuration) {

  val JwtSecretKey: String = config.get[String]("jwt.secretKey")
  val JwtAlgorithmType: JwtAlgorithm.HS256.type = JwtAlgorithm.HS256

  def createToken(claim: JsObject): String = {
    JwtJson.encode(claim, JwtSecretKey, JwtAlgorithmType)
  }

  def isValidToken(jwtToken: String): Boolean = {
    JwtJson.isValid(jwtToken, JwtSecretKey, Seq(JwtAlgorithmType))
  }

  def decodeClaim(jwtToken: String): Option[JsObject] = {
    val claim = JwtJson.decodeJson(jwtToken, JwtSecretKey, Seq(JwtAlgorithmType))
    claim.toOption
  }

}