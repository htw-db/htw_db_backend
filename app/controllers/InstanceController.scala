package controllers

import javax.inject._
import models.Instance
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.InstanceService

class InstanceController @Inject()(cc: ControllerComponents, instanceService: InstanceService) extends AbstractController(cc) {

  implicit val instanceFormat: OFormat[Instance] = Json.format[Instance]

  def getAll: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val instances = instanceService.listInstances()
    Ok(Json.toJson(instances))
  }
}
