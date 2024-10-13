package com.gadgetfactory.app.auth.domain.usecase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.gadgetfactory.app.auth.domain.model.AuthError
import com.gadgetfactory.app.auth.domain.model.AuthError.CredentialsNotReceived
import com.gadgetfactory.app.auth.domain.model.AuthError.UnknownAuthError
import com.gadgetfactory.app.auth.domain.model.GoogleCredential
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption

class GetGoogleCredential {

    private val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(SERVER_CLIENT_ID).build()

    private val request = GetCredentialRequest.Builder()
        .setCredentialOptions(listOf(googleIdOption))
        .build()

    suspend operator fun invoke(context: Context): Either<AuthError, GoogleCredential> = catch {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(request = request, context = context)
        val email = result.credential.data.getString(EMAIL_KEY).orEmpty()
        val token = result.credential.data.getString(TOKEN_KEY).orEmpty()
        either {
            ensure(email.isNotBlank()) { CredentialsNotReceived("Credentials not received") }
            ensure(token.isNotBlank()) { CredentialsNotReceived("Credentials not received") }
        }
        GoogleCredential(email, token)
    }.mapLeft { UnknownAuthError }
}

private const val SERVER_CLIENT_ID =
    "718845446517-btfo204pfq1ro544ls4gsjsjf3sk480f.apps.googleusercontent.com"
private const val EMAIL_KEY = "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID"
private const val TOKEN_KEY = "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
