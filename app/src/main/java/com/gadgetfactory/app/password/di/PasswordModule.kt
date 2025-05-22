package com.gadgetfactory.app.password.di

import com.gadgetfactory.app.password.PasswordScreenUiMapper
import com.gadgetfactory.app.password.PasswordViewModel
import com.gadgetfactory.app.password.mapper.InvalidPasswordSnackbarMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val passwordModule = module {
    factoryOf(::PasswordViewModel)
    factoryOf(::PasswordScreenUiMapper)
    factoryOf(::InvalidPasswordSnackbarMapper)
}
