package com.gadgetfactory.app.auth.domain.usecase

import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository

class CurrentUser(
    private val authRepo: AuthenticationRepository,
) {

    suspend operator fun invoke() = authRepo.getCurrentUser()
}
