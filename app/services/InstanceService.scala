package services

import com.google.inject.Inject
import forms.InstanceFormData
import models.{Instance, Person}
import repositories.InstanceRepository
import utils.Validators


class InstanceService @Inject()(instanceRepository: InstanceRepository, postgresService: PostgresService) {

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
    val databaseSuffix = instanceFormData.name
    val databasePrefix = person.username + "_"
    if (Validators.validateName(databaseSuffix)) {
      val databaseName = databasePrefix + databaseSuffix
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