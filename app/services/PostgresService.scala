package services

import com.google.inject.Inject
import play.api.Configuration
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{Await, ExecutionContext}


import scala.concurrent.duration.Duration

class PostgresService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, config: Configuration)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val groupName = config.get[String]("postgres.groupName")

  /**
   * Create a role in a group.
   * @param roleName  name of role
   * @return true if role was created
   */
  def createRoleInGroup(roleName: String): Boolean = {
    val statement = sql"""CREATE ROLE #${roleName} WITH NOSUPERUSER LOGIN CONNECTION LIMIT 10 IN ROLE #${groupName}"""
    try {
      val result = db.run(statement.as[(Int)])
      Await.result(result, Duration.Inf)
      true
    } catch {
      case e: Exception =>
        false
    }
  }

  /**
   * Creates a group for roles
   * @return true if group was created
   */
  def createGroup(): Boolean = {
    val statement = sql"""CREATE ROLE #${groupName} WITH NOSUPERUSER"""
    try {
      val result = db.run(statement.as[(Int)])
      Await.result(result, Duration.Inf)
      true
    } catch {
      case e: Exception =>
        false
    }
  }

  /**
   * Creates a database with ownership
   * @param databaseName name of database
   * @param roleName name of role for ownership
   * @return
   */
  def createDatabaseWithOwner(databaseName: String, roleName: String): Boolean = {
    val statement = sql"""CREATE DATABASE #${databaseName} WITH OWNER #${roleName}"""
    try {
      val query = db.run(statement.as[(Int)])
      Await.result(query, Duration.Inf)
      true
    } catch {
      case e: Exception =>
        false
    }
  }

  /**
   * Deletes a database
   * @param databaseName name of database
   * @return returns
   */
  def deleteDatabase(databaseName: String): Boolean = {
    val statement = sql"""DROP DATABASE IF EXISTS #$databaseName"""
    try {
      val query = db.run(statement.as[(Int)])
      Await.result(query, Duration.Inf)
      true
    } catch {
      case e: Exception =>
        false
    }
  }
}
