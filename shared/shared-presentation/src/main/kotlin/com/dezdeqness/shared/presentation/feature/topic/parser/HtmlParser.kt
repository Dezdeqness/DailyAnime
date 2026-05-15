package com.dezdeqness.shared.presentation.feature.topic.parser

import android.graphics.Typeface
import android.text.Html
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.dezdeqness.shared.presentation.feature.topic.model.ContentBlock
import com.dezdeqness.shared.presentation.utils.UrlNormalizer
import org.json.JSONObject

import javax.inject.Inject

class HtmlParser @Inject constructor(
    private val urlNormalizer: UrlNormalizer,
) {

    private val quoteStartRegex = Regex(
        """<(?:div\s+class="b-quote"|blockquote\s+class="b-quote-v2")>""",
    )

    private val quoteContentStartTag = Regex("""<div\s+class="quote-content">""")

    private val imageAnchorClassPattern = Regex("""\bclass="(?:[^"]*\s)?b-image(?:\s[^"]*)?"""")

    private val imgSrcPattern = Regex(
        """<img\b[^>]*?\bsrc="([^"]*)"[^>]*?/?>""",
        RegexOption.DOT_MATCHES_ALL,
    )

    fun parse(htmlBody: String): List<ContentBlock> {
        if (htmlBody.isBlank()) return emptyList()

        val blocks = mutableListOf<ContentBlock>()
        var remaining = htmlBody.trim()

        while (remaining.isNotEmpty()) {
            val quoteStart = quoteStartRegex.find(remaining)
            if (quoteStart != null) {
                val beforeQuote = remaining.substring(0, quoteStart.range.first)
                if (beforeQuote.isNotBlank()) {
                    blocks.addAll(parseInlineHtml(beforeQuote))
                }

                val afterQuoteOpen = remaining.substring(quoteStart.range.last + 1)
                val contentStart = quoteContentStartTag.find(afterQuoteOpen)
                if (contentStart != null) {
                    val contentBegin = contentStart.range.last + 1
                    val extracted = extractDivContent(afterQuoteOpen, contentBegin)
                    if (extracted != null) {
                        val (quoteContent, contentEndIndex) = extracted
                        blocks.add(ContentBlock.Quote(blocks = parseInlineHtml(quoteContent)))
                        val outerClose = findOuterClose(afterQuoteOpen, contentEndIndex)
                        remaining = afterQuoteOpen.substring(outerClose)
                    } else {
                        blocks.addAll(parseInlineHtml(remaining))
                        remaining = ""
                    }
                } else {
                    blocks.addAll(parseInlineHtml(afterQuoteOpen))
                    remaining = ""
                }
            } else {
                blocks.addAll(parseInlineHtml(remaining))
                remaining = ""
            }
        }

        return blocks.trimBreaks()
    }

    private fun extractDivContent(html: String, startIndex: Int): Pair<String, Int>? {
        var depth = 1
        var i = startIndex
        while (i < html.length && depth > 0) {
            val openDiv = html.indexOf("<div", i)
            val closeDiv = html.indexOf("</div>", i)

            if (closeDiv == -1) return null

            if (openDiv != -1 && openDiv < closeDiv) {
                depth++
                i = openDiv + 4
            } else {
                depth--
                if (depth == 0) {
                    return html.substring(startIndex, closeDiv) to (closeDiv + 6)
                }
                i = closeDiv + 6
            }
        }
        return null
    }

    private fun findOuterClose(html: String, fromIndex: Int): Int {
        val divClose = html.indexOf("</div>", fromIndex)
        val blockquoteClose = html.indexOf("</blockquote>", fromIndex)
        val end = when {
            divClose == -1 && blockquoteClose == -1 -> html.length
            divClose == -1 -> blockquoteClose + "</blockquote>".length
            blockquoteClose == -1 -> divClose + "</div>".length
            else -> if (divClose < blockquoteClose) divClose + "</div>".length else blockquoteClose + "</blockquote>".length
        }
        return end
    }

    private fun parseInlineHtml(html: String): List<ContentBlock> {
        val blocks = mutableListOf<ContentBlock>()

        val linkPattern = Regex(
            """<a\s[^>]*?(?:data-attrs="([^"]*)")?[^>]*?href="([^"]*)"[^>]*?(?:data-attrs="([^"]*)")?[^>]*?>(.*?)</a>""",
            RegexOption.DOT_MATCHES_ALL
        )

        var remaining = html
        while (remaining.isNotEmpty()) {
            val linkMatch = linkPattern.find(remaining)
            if (linkMatch != null) {
                val beforeLink = remaining.substring(0, linkMatch.range.first)
                if (beforeLink.isNotBlank()) {
                    blocks.addAll(parseTextWithBreaks(beforeLink))
                }

                val matchValue = linkMatch.value
                val openTagEnd = matchValue.indexOf('>')
                val openTag = if (openTagEnd != -1) matchValue.substring(0, openTagEnd) else matchValue
                val href = linkMatch.groupValues[2]
                val innerHtml = linkMatch.groupValues[4]

                val imageBlock = tryParseImageAnchor(openTag, innerHtml, href)
                if (imageBlock != null) {
                    blocks.add(imageBlock)
                    remaining = remaining.substring(linkMatch.range.last + 1)
                    continue
                }

                val dataAttrsRaw = (linkMatch.groupValues[1].ifEmpty { linkMatch.groupValues[3] })
                    .replace("&quot;", "\"")
                    .replace("&amp;", "&")
                val linkText = stripHtmlTags(innerHtml)

                var entityType: String? = null
                var entityId: Long? = null

                if (dataAttrsRaw.isNotEmpty()) {
                    try {
                        val json = JSONObject(dataAttrsRaw)
                        entityType = json.optString("type", null)
                        entityId = json.optLong("id", 0).takeIf { it != 0L }
                    } catch (_: Exception) {
                    }
                }

                if (linkText.isNotBlank()) {
                    blocks.add(
                        ContentBlock.Link(
                            text = linkText,
                            url = href,
                            entityType = entityType,
                            entityId = entityId,
                        )
                    )
                }

                remaining = remaining.substring(linkMatch.range.last + 1)
            } else {
                blocks.addAll(parseTextWithBreaks(remaining))
                remaining = ""
            }
        }

        return blocks
    }

    private fun tryParseImageAnchor(openTag: String, innerHtml: String, href: String): ContentBlock.Image? {
        if (!imageAnchorClassPattern.containsMatchIn(openTag)) return null
        val imgSrc = imgSrcPattern.find(innerHtml)?.groupValues?.get(1).orEmpty()
        if (imgSrc.isBlank()) return null
        return ContentBlock.Image(
            previewUrl = urlNormalizer.normalize(imgSrc),
            originalUrl = urlNormalizer.normalize(href.ifBlank { imgSrc }),
        )
    }

    private fun parseTextWithBreaks(html: String): List<ContentBlock> {
        val blocks = mutableListOf<ContentBlock>()
        val brParagraphPattern = Regex("""<br\s+class="br"\s*/?>""")

        val parts = brParagraphPattern.split(html)
        parts.forEachIndexed { index, part ->
            if (index > 0) {
                blocks.add(ContentBlock.ParagraphBreak)
            }
            val brPattern = Regex("""<br\s*/?>""")
            val subParts = brPattern.split(part)
            subParts.forEachIndexed { subIndex, subPart ->
                if (subIndex > 0) {
                    blocks.add(ContentBlock.LineBreak)
                }
                val textBlocks = parseStyledText(subPart)
                blocks.addAll(textBlocks)
            }
        }

        return blocks
    }

    private fun parseStyledText(html: String): List<ContentBlock> {
        val stripped = html
            .replace(Regex("""<span\s+class="name-en">[^<]*</span>"""), "")
            .replace(Regex("""<span\s+class="name-ru">([^<]*)</span>"""), "$1")
            .replace(Regex("""<img[^>]*class="smiley"[^>]*/?>"""), "")

        val spanned: Spanned = Html.fromHtml(stripped, Html.FROM_HTML_MODE_COMPACT)
        val text = spanned.toString().trim()
        if (text.isEmpty()) return emptyList()

        val blocks = mutableListOf<ContentBlock>()
        val styleSpans = spanned.getSpans(0, spanned.length, StyleSpan::class.java)
        val underlineSpans = spanned.getSpans(0, spanned.length, UnderlineSpan::class.java)

        data class StyledRange(val start: Int, val end: Int, val bold: Boolean, val italic: Boolean, val underline: Boolean)

        val ranges = mutableListOf<StyledRange>()
        for (span in styleSpans) {
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            val bold = span.style == Typeface.BOLD || span.style == Typeface.BOLD_ITALIC
            val italic = span.style == Typeface.ITALIC || span.style == Typeface.BOLD_ITALIC
            ranges.add(StyledRange(start, end, bold, italic, false))
        }
        for (span in underlineSpans) {
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            ranges.add(StyledRange(start, end, false, false, true))
        }

        if (ranges.isEmpty()) {
            blocks.add(ContentBlock.Text(text = text))
        } else {
            val sortedRanges = ranges.sortedBy { it.start }
            var pos = 0
            for (range in sortedRanges) {
                if (range.start > pos) {
                    val plainText = text.substring(pos, range.start)
                    if (plainText.isNotEmpty()) {
                        blocks.add(ContentBlock.Text(text = plainText))
                    }
                }
                val styledText = text.substring(range.start, minOf(range.end, text.length))
                if (styledText.isNotEmpty()) {
                    blocks.add(
                        ContentBlock.Text(
                            text = styledText,
                            bold = range.bold,
                            italic = range.italic,
                            underline = range.underline,
                        )
                    )
                }
                pos = minOf(range.end, text.length)
            }
            if (pos < text.length) {
                blocks.add(ContentBlock.Text(text = text.substring(pos)))
            }
        }

        return blocks
    }

    private fun stripHtmlTags(html: String): String {
        return html
            .replace(Regex("""<span\s+class="name-en">[^<]*</span>"""), "")
            .replace(Regex("""<span\s+class="name-ru">([^<]*)</span>"""), "$1")
            .replace(Regex("<[^>]*>"), "")
            .replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .trim()
    }

    private fun List<ContentBlock>.trimBreaks(): List<ContentBlock> {
        return this
            .dropWhile { it is ContentBlock.ParagraphBreak || it is ContentBlock.LineBreak }
            .dropLastWhile { it is ContentBlock.ParagraphBreak || it is ContentBlock.LineBreak }
    }
}
