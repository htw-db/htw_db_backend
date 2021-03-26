package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, _}

case class CredentialsFormData(username: String, password: String)

object CredentialsForm {
  val form: Form[CredentialsFormData] = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(CredentialsFormData.apply)(CredentialsFormData.unapply)
  )
}