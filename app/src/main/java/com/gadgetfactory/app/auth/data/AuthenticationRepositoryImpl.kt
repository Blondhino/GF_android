package com.gadgetfactory.app.auth.data

import arrow.core.Either
import com.gadgetfactory.app.auth.domain.model.AuthError
import com.gadgetfactory.app.auth.domain.model.AuthError.CredentialsNotReceived
import com.gadgetfactory.app.auth.domain.model.AuthError.UserNotFound
import com.gadgetfactory.app.auth.domain.model.GfUser
import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthenticationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRepository {
    override suspend fun signInWithCredentialToken(
        email: String,
        credentialToken: String,
    ): Either<AuthError, GfUser> = Either.catch {
        val credential = GoogleAuthProvider.getCredential(credentialToken, null)
        firebaseAuth.signInWithCredential(credential).await().user.toDomainOrThrow()
    }.mapLeft { CredentialsNotReceived(it.message.orEmpty()) }

    override suspend fun getCurrentUser(): Either<AuthError, GfUser> = Either.catch {
        firebaseAuth.currentUser.toDomainOrThrow()
    }.mapLeft { UserNotFound }

    override suspend fun signOut() = firebaseAuth.signOut()
}

private fun FirebaseUser?.toDomainOrThrow(): GfUser = this?.let {
    GfUser(
        id = uid,
        email = email.orEmpty(),
        firstName = displayName.orEmpty().substringBefore(" "),
        profileImageUrl = photoUrl.toString(),
    )
} ?: error("User data not found")
