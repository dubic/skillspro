package me.skillspro.auth

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import me.skillspro.auth.dto.CreateUserRequest
import me.skillspro.auth.dto.CreateUserResponse
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.verification.AccountVerificationService
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

//@WebMvcTest(UserController::class)
class UserControllerTest {
    @MockkBean
    var userService: UserService = mockk<UserService>(relaxed = true)

    @MockkBean
    var authService: AuthService = mockk<AuthService>()

    @MockkBean
    var accountVerificationService: AccountVerificationService = mockk<AccountVerificationService>()

    val account1 = CreateUserRequest("Dubic uzuegbu", "udubic@gmail.com", "dcami6299")

    @Test
    fun `create account`() {
        val userController = UserController(userService, authService, accountVerificationService)
        every {
            userService.createAccount(User(Name(account1.name), Email(account1.email, null)),
                    Password(account1.password)
            )
        } returns CreateUserResponse(false, 300)

        val createUserReponse = userController.createAccount(account1)

        assertThat(createUserReponse.statusCode, `is`(HttpStatus.OK))
        assertThat(createUserReponse.body?.verified, `is`(false))
        assertThat(createUserReponse.body?.tokenTtlSecs, `is`(notNullValue()))

    }
}