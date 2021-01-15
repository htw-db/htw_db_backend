package repositories

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.{Await, ExecutionContext}
import slick.jdbc.JdbcProfile
import scala.concurrent.duration.Duration

import models.{Person, PersonTable}


class PersonRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  var persons = TableQuery[PersonTable]

  def list(): Seq[Person] = {
    val result = db.run {
      persons.result
    }
    Await.result(result, Duration.Inf)
  }

  def create(username: String): Person = {
    val insertQuery = persons returning persons.map(_.id) into ((item, id) => item.copy(id = id))
    val action = insertQuery += Person(0, username)
    val result = db.run {
      action
    }
    Await.result(result, Duration.Inf)
  }

  def findByUsername(username: String): Option[Person] = {
    val result = db.run {
      persons.filter(person => person.username === username).result.headOption
    }
    Await.result(result, Duration.Inf)
  }
}