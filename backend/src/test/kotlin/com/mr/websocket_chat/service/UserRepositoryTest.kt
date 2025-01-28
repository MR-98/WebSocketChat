package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.repository.UserRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test

@DataJpaTest
class UserRepositoryTest(@Autowired val entityManager: TestEntityManager, @Autowired val repository: UserRepository) {

	@BeforeEach
	fun setUp() {
		entityManager.persist(UserEntity("username1", "Adam", "Kowalski", mutableListOf()))
		entityManager.persist(UserEntity("username2", "Jan", "Kowalski", mutableListOf()))
		entityManager.persist(UserEntity("username3", "Jan", "Testowy", mutableListOf()))
		entityManager.persist(UserEntity("username4", "Abc", "Abc", mutableListOf()))
	}


	@Test
	fun whenSearchedByUsername_thenReturnUser() {
		val user = repository.findByUsername("username1")
		assertThat(user).isNotNull()
		assertThat(user?.username).isEqualTo("username1")
		assertThat(user?.firstName).isEqualTo("Adam")
		assertThat(user?.lastName).isEqualTo("Kowalski")
	}

	@Test
	fun whenSearchedByFullName_thenReturnListOfUsers() {
		val list = repository.findByFullName("Adam Kowalski")
		assertThat(list.isNotEmpty()).isTrue()
		assertThat(list[0].username).isEqualTo("username1")
		assertThat(list[0].firstName).isEqualTo("Adam")
		assertThat(list[0].lastName).isEqualTo("Kowalski")
	}

	@Test
	fun whenSearchedByFullName_caseInsensitive_thenReturnListOfUsers() {
		val list = repository.findByFullName("adam kowalski")
		assertThat(list.isNotEmpty()).isTrue()
		assertThat(list[0].username).isEqualTo("username1")
		assertThat(list[0].firstName).isEqualTo("Adam")
		assertThat(list[0].lastName).isEqualTo("Kowalski")
	}

	@Test
	fun whenSearchedByOnlyFirstName_thenReturnListOfUsers() {
		val list = repository.findByFullName("Adam")
		assertThat(list.isNotEmpty()).isTrue()
		assertThat(list[0].username).isEqualTo("username1")
		assertThat(list[0].firstName).isEqualTo("Adam")
		assertThat(list[0].lastName).isEqualTo("Kowalski")
	}

	@Test
	fun whenSearchedByOnlyLastName_thenReturnListOfUsers() {
		val list = repository.findByFullName("Testowy")
		assertThat(list.isNotEmpty()).isTrue()
		assertThat(list[0].username).isEqualTo("username3")
		assertThat(list[0].firstName).isEqualTo("Jan")
		assertThat(list[0].lastName).isEqualTo("Testowy")
	}

	@Test
	fun whenSearchedByNotExistingFullName_thenReturnEmptyListOfUsers() {
		val list = repository.findByFullName("Non Existing User")
		assertThat(list.isEmpty()).isTrue()
	}

	@Test
	fun whenSearchedByEmptyFullName_thenReturnEntireListOfUsers() {
		val list = repository.findByFullName("")
		assertThat(list.size).isEqualTo(4)
	}


}