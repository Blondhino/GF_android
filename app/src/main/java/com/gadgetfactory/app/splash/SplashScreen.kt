package com.gadgetfactory.app.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.R
import com.gadgetfactory.app.home.HomeScreen
import com.gadgetfactory.app.ui.components.BodyMediumText
import com.gadgetfactory.app.ui.components.Image
import com.gadgetfactory.app.ui.components.ImageType.Resource

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SplashViewModel = koinScreenModel()
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                imageType = Resource(R.drawable.ic_app_icon_with_background),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.Center),
            )
            BodyMediumText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 120.dp),
                text = "Splash screen + ${viewModel.getText()}",
            )
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                onClick = { navigator.push(HomeScreen()) },
            ) {
                Text("Go to Home !")
            }
        }
    }
}
