package com.gadgetfactory.app.auth.di

import com.gadgetfactory.app.auth.data.AuthenticationRepositoryImpl
import com.gadgetfactory.app.auth.domain.repo.AuthenticationRepository
import com.gadgetfactory.app.auth.domain.usecase.CurrentUser
import com.gadgetfactory.app.auth.domain.usecase.GetGoogleCredential
import com.gadgetfactory.app.auth.domain.usecase.LoginWithGoogle
import com.gadgetfactory.app.auth.domain.usecase.Logout
import com.gadgetfactory.app.auth.ui.AuthViewModel
import com.gadgetfactory.app.auth.ui.mapper.AuthScreenUiMapper
import com.gadgetfactory.app.auth.ui.mapper.GoogleLoginSnackErrorMapper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    factoryOf(::AuthViewModel)
    factoryOf(::GetGoogleCredential)
    single { Firebase.auth }
    factoryOf(::AuthenticationRepositoryImpl) bind AuthenticationRepository::class
    factoryOf(::LoginWithGoogle)
    factoryOf(::CurrentUser)
    factoryOf(::Logout)
    factoryOf(::GoogleLoginSnackErrorMapper)
    factoryOf(::AuthScreenUiMapper)
}
