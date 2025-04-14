package com.dezdeqness.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme

private const val MAX_LINES_COLLAPSED = 5

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
) {
    val labelStyle = SpanStyle(
        fontWeight = AppTheme.typography.bodyMedium.fontWeight,
        fontSize = AppTheme.typography.bodyMedium.fontSize,
        fontStyle = AppTheme.typography.bodyMedium.fontStyle,
        fontFamily = AppTheme.typography.bodyMedium.fontFamily,
        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
    )

    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .clickable(clickable) {
                isExpanded = !isExpanded
            }
            .then(modifier)
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = labelStyle) {
                            append(stringResource(R.string.details_synopsis_label_show_less))
                        }
                    } else {
                        val showMore = stringResource(R.string.details_synopsis_label_show_more)
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMore.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = labelStyle) { append(showMore) }
                    }
                } else {
                    append(text)
                }
            },
            maxLines = if (isExpanded) Int.MAX_VALUE else MAX_LINES_COLLAPSED,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(MAX_LINES_COLLAPSED - 1)
                }
            },
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = AppTheme.colors.textPrimary
        )
    }
}
