package com.gadgetfactory.app.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.gadgetfactory.app.R

val gfFont = FontFamily(
    Font(resId = R.font.montserrat_regular, weight = Normal),
    Font(resId = R.font.montserrat_semibold, weight = SemiBold),
    Font(resId = R.font.montserrat_bold, weight = Bold),
    Font(resId = R.font.montserrat_medium, weight = Medium),
)

val BodySmallTextStyle = TextStyle(
    fontFamily = gfFont,
    fontSize = 14.sp,
    lineHeight = 16.sp,
    fontWeight = Normal,
)

val BodyMediumTextStyle = TextStyle(
    fontFamily = gfFont,
    fontSize = 16.sp,
    lineHeight = 18.sp,
    fontWeight = Normal,
)

val BodyLargeTextStyle = TextStyle(
    fontFamily = gfFont,
    fontSize = 18.sp,
    lineHeight = 20.sp,
    fontWeight = Normal,
)

val TitleLargeTextStyle = TextStyle(
    fontFamily = gfFont,
    fontSize = 22.sp,
    lineHeight = 24.sp,
    fontWeight = SemiBold,
)

@Composable
fun BodySmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = BodySmallTextStyle.lineHeight,
    fontWeight: FontWeight? = BodySmallTextStyle.fontWeight,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        style = BodySmallTextStyle,
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = BodyMediumTextStyle.lineHeight,
    fontWeight: FontWeight? = BodyMediumTextStyle.fontWeight,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        style = BodyMediumTextStyle,
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
fun BodyLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = BodyLargeTextStyle.lineHeight,
    fontWeight: FontWeight? = BodyLargeTextStyle.fontWeight,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        style = BodyLargeTextStyle,
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
fun TitleLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TitleLargeTextStyle.lineHeight,
    fontWeight: FontWeight? = TitleLargeTextStyle.fontWeight,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight,
        style = TitleLargeTextStyle,
        maxLines = maxLines,
        overflow = overflow,
    )
}
