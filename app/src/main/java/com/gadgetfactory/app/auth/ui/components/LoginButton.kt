package com.gadgetfactory.app.auth.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.R
import com.gadgetfactory.app.ui.components.BodyMediumText
import com.gadgetfactory.app.ui.components.Image
import com.gadgetfactory.app.ui.components.ImageType

@Composable
fun LoginButton(
    data: LoginButtonData,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    onClick: (AuthProvider) -> Unit,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = shape,
            )
            .clip(shape)
            .clickable(onClick = { onClick(data.provider) })
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        data.providerIcon?.let {
            Image(
                modifier = Modifier.size(32.dp).align(Alignment.CenterStart),
                imageType = ImageType.Resource(data.providerIcon),
            )
        }
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            BodyMediumText(text = stringResource(R.string.auth_screen_login_button_general_text))
            BodyMediumText(text = stringResource(data.buttonText), fontWeight = Bold)
        }
    }
}

sealed class LoginButtonData(
    @DrawableRes val providerIcon: Int?,
    @StringRes val buttonText: Int,
    val provider: AuthProvider,
) {
    data class GoogleLoginButtonData(
        @DrawableRes val icon: Int = R.drawable.ic_google_logo,
        @StringRes val text: Int = R.string.auth_screen_google_login_button_text,
    ) : LoginButtonData(providerIcon = icon, buttonText = text, provider = AuthProvider.Google)
}

sealed class AuthProvider(val providerCode: String) {
    data object Google : AuthProvider("provider_google")
}
