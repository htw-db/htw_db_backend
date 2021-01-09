package services

import com.google.inject.Inject

import models.Person
import repositories.PersonRepository


class PersonService @Inject()(personRepository: PersonRepository) {

  def listPersons(): Seq[Person] = {
    personRepository.list()
  }

  def addPerson(username: String): Person = {
    personRepository.create(username)
  }

  def findPersonByUsername(username: String): Option[Person] = {
    personRepository.findByUsername(username)
  }
}