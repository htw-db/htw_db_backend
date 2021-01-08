package services

import com.google.inject.Inject
import scala.concurrent.Future

import models.Person
import repositories.PersonRepository


class PersonService @Inject()(personRepository: PersonRepository) {

  def listPersons(): Future[Seq[Person]] = {
    personRepository.list()
  }
}