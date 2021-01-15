package forms

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._

case class InstanceFormData(name: String)

object InstanceForm {
  val form: Form[InstanceFormData] = Form(
    mapping(
      "name" -> nonEmptyText,
    )(InstanceFormData.apply)(InstanceFormData.unapply)
  )
}