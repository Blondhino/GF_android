package com.gadgetfactory.app.auth.domain.usecase

import arrow.core.Either
import com.gadgetfactory.app.auth.domain.model.AuthError
import com.gadgetfactory.app.auth.domain.model.GfUser
import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository

class LoginWithGoogle(
    private val authRepo: AuthenticationRepository,
) {
    suspend operator fun invoke(
        email: String,
        token: String,
    ): Either<AuthError, GfUser> =
        authRepo.signInWithCredentialToken(email, token)
}
