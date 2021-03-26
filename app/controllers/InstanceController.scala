package controllers

import actions.{AuthAction, UserRequest}
import forms.InstanceForm
import play.api.{Logger, Logging}
import javax.inject._
import models.Instance
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.InstanceService

class InstanceController @Inject()(cc: ControllerComponents, instanceService: InstanceService, authAction: AuthAction) extends AbstractController(cc) {

  implicit val instanceFormat: OFormat[Instance] = Json.format[Instance]
  val logger: Logger = Logger(this.getClass)

  def getAll: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    val instances = instanceService.listInstances()
    Ok(Json.toJson(instances))
  }

  def filterByPerson: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    logger.info(request.remoteAddress + " - " + request.person.username + " - " + request)
    val instances = instanceService.filterInstancesByPerson(request.person)
    Ok(Json.toJson(instances))
  }

  def create: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    logger.info(request.remoteAddress + " - " + request.person.username + " - " + request)
    InstanceForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(msg => logger.error(request.remoteAddress + " - " + request.person.username + " - " + request + " - " + msg))
        BadRequest("")
      },
      instanceFormData => {
        val instance = instanceService.addInstance(instanceFormData, request.person)
        if (instance.isDefined) {
          Ok(Json.toJson(instance))
        } else {
          logger.error(request.remoteAddress + " - " + request.person.username + " - " + request + " - instance not defined")
          BadRequest("")
        }
      })
  }

  def delete(id: Long): Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    logger.info(request.remoteAddress + " - " + request.person.username + " - " + request)
    val result = instanceService.deleteInstance(id, request.person)
    if(result.getOrElse(0) != 0) {
      Ok(Json.toJson(result))
    } else {
      logger.error(request.remoteAddress + " - " + request.person.username + " - " + request + " - could not delete instance")
      BadRequest("")
    }
  }
}
