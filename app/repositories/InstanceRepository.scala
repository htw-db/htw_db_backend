package repositories

import com.google.inject.Inject
import models.{Instance, InstanceTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

class InstanceRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val instances = TableQuery[InstanceTable]

  def list(): Seq[Instance] = {
    val result = db.run {
      instances.result
    }
    Await.result(result, Duration.Inf)
  }

  def create(name: String, personId: Int): Instance = {
    val insertQuery = instances returning instances.map(_.id) into ((item, id) => item.copy(id = id))
    val action = insertQuery += Instance(0, name, personId)
    val result = db.run {
      action
    }
    Await.result(result, Duration.Inf)
  }
}
