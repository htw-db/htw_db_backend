package services

import com.google.inject.Inject
import forms.InstanceFormData
import models.{Instance, Person}
import repositories.InstanceRepository


class InstanceService @Inject()(instanceRepository: InstanceRepository) {
  def listInstances(): Seq[Instance] = {
    instanceRepository.list()
  }
  def addInstance(instanceFormData: InstanceFormData, person: Person): Option[Instance] = {
    val name = person.username + "_" + instanceFormData.name
    instanceRepository.create(name, person.id)
  }
  def deleteInstance(id: Long, person: Person): Option[Int] = {
    instanceRepository.delete(id, person.id)
  }
}