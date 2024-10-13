package com.gadgetfactory.app.auth.domain.repo

import arrow.core.Either
import com.gadgetfactory.app.auth.domain.model.AuthError
import com.gadgetfactory.app.auth.domain.model.GfUser

interface AuthenticationRepository {
    suspend fun signInWithCredentialToken(
        email: String,
        credentialToken: String,
    ): Either<AuthError, GfUser>

    suspend fun getCurrentUser(): Either<AuthError, GfUser>

    suspend fun signOut()
}
