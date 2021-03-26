package services

import com.google.inject.Inject
import play.api.{Configuration, Logger}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{JdbcProfile, SQLActionBuilder}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

class PostgresService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, config: Configuration)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val logger: Logger = Logger(this.getClass)
  val groupName: String = config.get[String]("postgres.groupName")

  /**
   * Runs statement as query
   * @param statement which will turn in to a query
   * @return true if success
   */
  def run_statement(statement: SQLActionBuilder): Boolean = {
    try {
      val query = db.run(statement.as[Int])
      Await.result(query, Duration.Inf)
      true
    } catch {
      case e: Exception =>
        logger.error(e.toString)
        false
    }
  }

  /**
   * Create a role in a group.
   * @param roleName  name of role
   * @return true if role was created
   */
  def createRoleInGroup(roleName: String): Boolean = {
    val statement = sql"""CREATE ROLE #${roleName} WITH NOSUPERUSER LOGIN CONNECTION LIMIT 10 IN ROLE #${groupName}"""
    run_statement(statement)
  }

  /**
   * Creates a group for roles
   * @return true if group was created
   */
  def createGroup(): Boolean = {
    val statement = sql"""CREATE ROLE #${groupName} WITH NOSUPERUSER"""
    run_statement(statement)
  }

  /**
   * Creates a database with ownership
   * @param databaseName name of database
   * @param roleName name of role for ownership
   * @return
   */
  def createDatabaseWithOwner(databaseName: String, roleName: String): Boolean = {
    val statement = sql"""CREATE DATABASE #${databaseName} WITH OWNER #${roleName}"""
    run_statement(statement)
  }

  /**
   * Deletes a database
   * @param databaseName name of database
   * @return returns
   */
  def deleteDatabase(databaseName: String): Boolean = {
    val statement = sql"""DROP DATABASE IF EXISTS #$databaseName"""
    run_statement(statement)
  }

}
