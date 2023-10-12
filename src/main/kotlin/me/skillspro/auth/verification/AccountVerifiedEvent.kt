package me.skillspro.auth.verification

import me.skillspro.auth.models.User

data class AccountVerifiedEvent(val user: User)
