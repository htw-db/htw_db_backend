package controllers

import javax.inject._
import play.api.Logger
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PingController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val logger: Logger = Logger(this.getClass)

  def ping(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    logger.info("PingController was reached!" + System.currentTimeMillis())
    Ok("Pong!")
  }
}