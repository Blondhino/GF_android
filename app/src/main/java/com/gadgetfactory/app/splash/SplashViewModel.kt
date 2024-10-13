package com.gadgetfactory.app.splash

import arrow.core.Either
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gadgetfactory.app.auth.domain.usecase.CurrentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val currentUser: CurrentUser,
) : ScreenModel {
    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination = _startDestination
        .onStart { calculateInitialDestination() }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    private fun calculateInitialDestination() = screenModelScope.launch {
        when (currentUser()) {
            is Either.Left -> _startDestination.update { StartDestination.Auth }
            is Either.Right -> _startDestination.update { StartDestination.GadgetCenter }
        }
    }
}

sealed interface StartDestination {
    data object Auth : StartDestination
    data object GadgetCenter : StartDestination
}
