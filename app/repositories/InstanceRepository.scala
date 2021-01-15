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

  /**
   * List all instances
   *
   * @return all instances
   */
  def list(): Seq[Instance] = {
    val result = db.run {
      instances.result
    }
    Await.result(result, Duration.Inf)
  }

  /**
   * List instances with the same personId
   * @param personId id of person
   * @return list of filtered instances
   */
  def filterByPerson(personId: Long): Seq[Instance] = {
    try {
      val result = db.run {
        instances.filter(_.personId === personId).result
      }
      Await.result(result, Duration.Inf)
    } catch {
      case e: Exception =>
        Seq()
    }
  }

  /**
   * Creates an instance
   *
   * @param name     name of instance
   * @param personId name of person
   * @return instance if created else None
   */
  def create(name: String, personId: Long): Option[Instance] = {
    val insertQuery = instances returning instances.map(_.id) into ((item, id) => item.copy(id = id))
    try {
      val result = db.run {
        insertQuery += Instance(0, name, personId)
      }
      Some(Await.result(result, Duration.Inf))
    } catch {
      case e: Exception =>
        None
    }
  }

  /**
   * Deletes an instance
   *
   * @param id       id of instance
   * @param personId id of person
   * @return number of deleted rows
   */
  def delete(id: Long, personId: Long): Option[Int] = {
    try {
      val result = db.run {
        instances.filter(_.id === id).filter(_.personId === personId).delete
      }
      Some(Await.result(result, Duration.Inf))
    } catch {
      case e: Exception =>
        None
    }
  }
}
