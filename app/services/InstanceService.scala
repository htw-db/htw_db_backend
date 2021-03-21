package services

import com.google.inject.Inject

import forms.InstanceFormData
import models.{Instance, Person}
import repositories.InstanceRepository


class InstanceService @Inject()(instanceRepository: InstanceRepository, postgresService: PostgresService) {

  private def onlyLegalChars(text: String): Boolean = {
    text.forall(_.isLetterOrDigit)
  }

  private def hasLegalLength(text: String): Boolean = {
    text.length >= 4 && text.length <= 20
  }

  private def onlyLowerCase(text: String): Boolean = {
    text.forall(_.isLower)
  }

  /**
   * List all instances using a repository
   *
   * @return instances
   */
  def listInstances(): Seq[Instance] = {
    instanceRepository.list()
  }

  /**
   * Get instance by id using a repository
   *
   * @param id of instance
   * @return instance if exists
   */
  def getInstance(id: Long): Option[Instance] = {
    instanceRepository.get(id)
  }

  /**
   * Filter all instances by person using a repository
   *
   * @param person person object to filer
   * @return filtered instances
   */
  def filterInstancesByPerson(person: Person): Seq[Instance] = {
    instanceRepository.filterByPerson(person.id)
  }

  /**
   * Creates an instance using a repository
   *
   * @param instanceFormData instance data
   * @param person           person data
   * @return instance if created
   */
  def addInstance(instanceFormData: InstanceFormData, person: Person): Option[Instance] = {
    if (onlyLegalChars(instanceFormData.name) && hasLegalLength(instanceFormData.name) && onlyLowerCase(instanceFormData.name)) {
      val databaseName = person.username + "_" + instanceFormData.name
      val result = instanceRepository.create(databaseName, person.id)
      if (result.isDefined) {
        postgresService.createDatabaseWithOwner(databaseName, person.username)
      }
      result
    } else {
      None
    }
  }

  /**
   * Delete an instance using a repository
   *
   * @param id     id of instance
   * @param person id of person
   * @return number of deleted rows
   */
  def deleteInstance(id: Long, person: Person): Option[Int] = {
    val instance = getInstance(id)
    if (instance.isDefined && instance.get.personId == person.id) {
      val result = postgresService.deleteDatabase(instance.get.name)
      if (result) {
        instanceRepository.delete(id, person.id)
      } else {
        None
      }
    } else {
      None
    }
  }
}