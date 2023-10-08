package me.skillspro.auth.session

import jakarta.servlet.Filter
import me.skillspro.auth.UserService
import me.skillspro.auth.dao.DBUser
import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.CreateUserRequest
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue


@SpringBootTest
@Testcontainers
class UserControllerTest {
    //    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var userRepo: UserRepo

    val account1 = CreateUserRequest("Dubic uzuegbu", "udubic@gmail.com", "dcami6299")

    companion object {
        @ServiceConnection
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:7.0.2")

        @JvmStatic
        @BeforeAll
        fun setUp(): Unit {
            mongoDBContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mongoDBContainer.stop()
        }
    }

    @BeforeEach
    fun setUpEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply { SecurityMockMvcConfigurers.springSecurity() }
                .build()
    }

    @Test
    fun createAccount() {
        this.mockMvc.perform(post("/users")
                .contentType("application/json").content(account1.toJson())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified", `is`(false)))

        val savedUser = userRepo.findByIdOrNull(account1.email)
        assertThat(savedUser, `is`(notNullValue()))
    }

    @Test
    fun `duplicate account create`() {
        //given account with same email exists
//        userRepo.save(DBUser(account1.name, account1.email, "1jua88", false))
//        println("SAVED ::::::::::: ${userRepo.findByIdOrNull(account1.email)}")
        //when
        this.mockMvc.perform(post("/users")
                .contentType("application/json").content(account1.toJson())).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", `is`("user ${account1.email} already exists")))
    }
}