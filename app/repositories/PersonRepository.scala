package repositories

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import models.{Person, PersonTable}

class PersonRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  var persons = TableQuery[PersonTable]

  def list(): Future[Seq[Person]] = {
    db.run {
      persons.result
    }
  }
}