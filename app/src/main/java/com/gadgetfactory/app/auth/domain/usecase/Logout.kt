package com.gadgetfactory.app.auth.domain.usecase

import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository

class Logout(
    private val authRepo: AuthenticationRepository,
) {
    suspend operator fun invoke() = authRepo.signOut()
}
