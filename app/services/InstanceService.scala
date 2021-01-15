package services

import com.google.inject.Inject
import forms.InstanceFormData
import models.{Instance, Person}
import repositories.InstanceRepository


class InstanceService @Inject()(instanceRepository: InstanceRepository) {
  /**
   * List all instances using a repository
   * @return instances
   */
  def listInstances(): Seq[Instance] = {
    instanceRepository.list()
  }

  /**
   * Filter all instances by person using a repository
   * @param person person object to filer
   * @return filtered instances
   */
  def filterInstancesByPerson(person: Person): Seq[Instance] = {
    instanceRepository.filterByPerson(person.id)
  }

  /**
   * Create an instance using a repository
   * @param instanceFormData instance data
   * @param person person data
   * @return instance if created
   */
  def addInstance(instanceFormData: InstanceFormData, person: Person): Option[Instance] = {
    val name = person.username + "_" + instanceFormData.name
    instanceRepository.create(name, person.id)
  }

  /**
   * Delete an instance using a repository
   * @param id id of instance
   * @param person id of person
   * @return number of deleted rows
   */
  def deleteInstance(id: Long, person: Person): Option[Int] = {
    instanceRepository.delete(id, person.id)
  }
}