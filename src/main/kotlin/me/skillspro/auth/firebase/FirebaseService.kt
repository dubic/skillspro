package me.skillspro.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FirebaseService {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun verifyAccount(idToken: String): GoogleAccount {
        logger.debug("About to verify account from firebase")
        val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)
        return GoogleAccount(
                displayName = decodedToken.name,
                email = decodedToken.email,
                photoUrl = decodedToken.picture,
                id = decodedToken.uid
        )
    }
}