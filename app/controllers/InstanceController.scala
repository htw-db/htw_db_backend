package controllers

import actions.{AuthAction, UserRequest}
import forms.InstanceForm
import javax.inject._
import models.Instance
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import services.InstanceService

class InstanceController @Inject()(cc: ControllerComponents, instanceService: InstanceService, authAction: AuthAction) extends AbstractController(cc) {

  implicit val instanceFormat: OFormat[Instance] = Json.format[Instance]

  def getAll: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    val instances = instanceService.listInstances()
    Ok(Json.toJson(instances))
  }

  def filterByPerson: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    val instances = instanceService.filterInstancesByPerson(request.person)
    Ok(Json.toJson(instances))
  }

  def create: Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    InstanceForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        BadRequest("")
      },
      instanceFormData => {
        val instance = instanceService.addInstance(instanceFormData, request.person)
        if (instance.isDefined) {
          Ok(Json.toJson(instance))
        } else {
          BadRequest("")
        }
      })
  }

  def delete(id: Long): Action[AnyContent] = authAction { implicit request: UserRequest[AnyContent] =>
    val result = instanceService.deleteInstance(id, request.person)
    if(result.getOrElse(0) != 0) {
      Ok(Json.toJson(result))
    } else {
      BadRequest("")
    }
  }
}
