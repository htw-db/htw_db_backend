package models

import slick.jdbc.MySQLProfile.api._


case class Instance(id: Long = 0L, name: String, personId: Long)

class InstanceTable(tag: Tag) extends Table[Instance](tag, "instance") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def personId = column[Long]("person_id")

  def * = (id, name, personId).mapTo[Instance]

  def person =
    foreignKey("person", personId, TableQuery[InstanceTable])(_.id, onDelete = ForeignKeyAction.Cascade)
}