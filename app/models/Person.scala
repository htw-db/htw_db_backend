package models

import slick.jdbc.MySQLProfile.api._

case class Person (id: Long = 0L, username: String)

class PersonTable(tag: Tag) extends Table[Person](tag, "person") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")

  def * = (id, username).mapTo[Person]
}