package services

import com.google.inject.Inject
import models.Instance
import repositories.InstanceRepository


class InstanceService @Inject()(instanceRepository: InstanceRepository) {
  def listInstances(): Seq[Instance] = {
    instanceRepository.list()
  }
  def addInstance(name: String, personId: Int): Instance = {
    instanceRepository.create(name, personId)
  }
}