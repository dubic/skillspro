package me.skillspro.auth

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import me.skillspro.auth.dao.DBUser
import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.SocialLoginRequest
import me.skillspro.auth.firebase.FirebaseService
import me.skillspro.auth.firebase.GoogleAccount
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.TokenService
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.tokens.models.TokenType
import me.skillspro.core.config.ConfigProperties
import me.skillspro.core.storage.StorageService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {
    val account = User(
        name = Name("Dubic Uzuegbu"),
        email = Email("udubic@gmail.com", verified = false),
        photoUrl = null
    )
    val dbUser =
        DBUser(account.name.value, account.email.value, "1200288-1992827-1882", false, null)

    val configProperties = ConfigProperties()
    var userRepo = mockk<UserRepo>(relaxed = true)
    var passwordEncoder = mockk<PasswordEncoder>(relaxed = true)
    var events = mockk<ApplicationEventPublisher>(relaxed = true)
    var firebaseService = mockk<FirebaseService>(relaxed = true)
    var tokenService = mockk<TokenService>(relaxed = true)
    var storageService = mockk<StorageService>(relaxed = true)

    val userService = UserService(
        userRepo = userRepo,
        configProperties = configProperties,
        events = events,
        passwordEncoder = passwordEncoder,
        tokenService = tokenService,
        firebaseService = firebaseService,
        storageService = storageService,
    )

    @BeforeEach
    fun init() {
        every { passwordEncoder.encode("dcamic60991") } returns "1200288-1992827-1882"

        every { userRepo.save(any()) } returns dbUser

        every { events.publishEvent(any()) } just runs
    }


    @Test
    fun createAccount() {
        //given
        accountWithEmailDoesNotExists(account.email.value)

        val createUserResponse = userService.createAccount(
            user = account,
            password = Password("dcamic60991")
        )
        assertThat(createUserResponse.verified, `is`(false))
        assertThat(createUserResponse.tokenTtlSecs, `is`(configProperties.redisTokenTtlSecs))
    }

    @Test
    fun `Account exists fails on create`() {
        //given
        accountWithEmailExists(account.email.value)
        //when
        assertThrows<IllegalArgumentException> {
            userService.createAccount(
                user = account,
                password = Password("dcamic60991")
            )
        }
    }

    @Test
    fun `find account that does not exist`() {
        //given
        accountWithEmailDoesNotExists(account.email.value)
        //when
        assertThrows<NoSuchElementException> {
            userService.findAccount(account.email)
        }
    }

    @Test
    fun findAccount() {
        //given
        accountWithEmailExists(account.email.value)
        //when
        val user = userService.findAccount(account.email)
        assertThat(user, notNullValue(User.javaClass))
    }

    @Test
    fun getOrCreateUser() {
        //given
        every { firebaseService.verifyAccount(any()) } returns GoogleAccount(
            displayName = account.name.value,
            email = account.email.value,
            photoUrl = account.photoUrl,
            id = "account id"
        )
        accountWithEmailExists(account.email.value)
        //when
        val user = userService.getOrCreateUser(SocialLoginRequest("id token"))
        assertThat(user, notNullValue(User.javaClass))
    }

    @Test
    fun `verifyTokenAndResetPassword invalid token`() {
        //given
        tokenIsValid("123456")
        //when
        assertThrows<IllegalArgumentException> {
            userService.verifyTokenAndResetPassword(
                account.email, Password("password"), Token("wrong")
            )
        }
    }

    @Test
    fun verifyTokenAndResetPassword() {
        //given
        accountWithEmailExists(account.email.value)
        tokenIsValid("123456")
        //when
        assertDoesNotThrow {
            userService.verifyTokenAndResetPassword(
                account.email, Password("password"), Token("123456")
            )
        }
    }

    private fun accountWithEmailExists(email: String) {
        every {
            userRepo.findByIdOrNull(email)
        } returns dbUser
    }

    private fun accountWithEmailDoesNotExists(email: String) {
        every {
            userRepo.findByIdOrNull(email)
        } returns null
    }

    private fun tokenIsValid(token: String) {
        every {
            tokenService.isTokenValid(
                account.email,
                Token(token),
                TokenType.PASSWORD
            )
        } returns true
    }

    @Test
    fun addProfilePhoto() {
        //given
        accountWithEmailExists(account.email.value)
        every { storageService.store(any(), any()) } returns
                "https://localhost:7200/content/profile"
        //when
        val url = userService.addProfilePhoto(
            MockMultipartFile("profile.jpg", "String".toByteArray()), account)

        assertThat(url, notNullValue())
        assertThat(url, startsWithIgnoringCase("http"))
        assertThat(url, containsStringIgnoringCase("profile"))
    }

}