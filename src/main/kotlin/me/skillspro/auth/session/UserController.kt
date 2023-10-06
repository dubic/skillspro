package me.skillspro.auth.session

import me.skillspro.auth.UserService
import me.skillspro.auth.dto.CreateUserRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.core.BaseController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService): BaseController() {
    @PostMapping
    fun createAccount(@RequestBody createUserRequest: CreateUserRequest): Any {
        val user = User(
                Name(createUserRequest.name),
                Email(createUserRequest.email, false)
        )
        val createdUser = this.userService.createAccount(
                user,
                Password(createUserRequest.password),
        );
        return object {
            val verified = user.isVerified()
        }
    }

}