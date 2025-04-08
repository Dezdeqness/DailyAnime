package com.dezdeqness.presentation.features.animedetails.composables.list

import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.dezdeqness.R
import com.dezdeqness.core.ui.ExpandableText
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.models.DescriptionUiModel

@Composable
fun DetailsDescription(
    modifier: Modifier = Modifier,
    description: DescriptionUiModel,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(R.string.anime_details_description_title),
            style = AppTheme.typography.headlineMedium,
            color = AppTheme.colors.textPrimary,
        )

        val spanned = remember(description.content) {
            HtmlCompat.fromHtml(description.content, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
        }

        val annotatedString = remember(spanned) {
            buildAnnotatedString {
                append(spanned.toString())

                spanned.getSpans(0, spanned.length, CharacterStyle::class.java).forEach { span ->
                    val start = spanned.getSpanStart(span)
                    val end = spanned.getSpanEnd(span)
                    when (span) {
                        is StyleSpan -> when (span.style) {
                            Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                            Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                        }
                        is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                    }
                }
            }
        }

        ExpandableText(text = annotatedString)
    }
}
