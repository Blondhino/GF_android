package com.gadgetfactory.app.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gadgetfactory.app.core.ui.theme.SilverMist

@Composable
fun PasswordTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val customSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary,
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
    )
    val keyboard = LocalSoftwareKeyboardController.currentOrThrow
    var isPasswordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        if (isFocused) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f) else SilverMist,
        label = "borderColorAnimation",
    )
    val containerAlpha by animateFloatAsState(if (enabled) 1f else 0.2f)

    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
        TextField(
            value = value,
            onValueChange = onValueChanged,
            enabled = enabled,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(25),
                    color = borderColor,
                )
                .alpha(containerAlpha),
            textStyle = getTextStyle(TextStyleType.BodySmall),
            placeholder = {
                BodySmallText(
                    text = "Password",
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                )
            },
            shape = RoundedCornerShape(25),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                cursorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = SilverMist,
                focusedContainerColor = SilverMist,
            ),
            interactionSource = interactionSource,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboard.hide()
                    onDone()
                },
            ),
            singleLine = true,
        )
    }
}
