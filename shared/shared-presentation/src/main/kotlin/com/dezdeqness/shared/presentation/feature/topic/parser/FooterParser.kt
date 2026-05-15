package com.dezdeqness.shared.presentation.feature.topic.parser

import com.dezdeqness.shared.presentation.feature.topic.model.ContentBlock
import com.dezdeqness.shared.presentation.utils.UrlNormalizer

import javax.inject.Inject

class FooterParser @Inject constructor(
    private val urlNormalizer: UrlNormalizer,
) {

    private val videoRegex = Regex(
        """<div\s+class="b-video\s+unprocessed\s+(\w+)">\s*<a\s+class="video-link"\s+data-href="([^"]*)"[^>]*>\s*<img[^>]*src="([^"]*)"[^>]*>\s*</a>\s*(?:<span\s+class="name"[^>]*>([^<]*)</span>)?""",
        RegexOption.DOT_MATCHES_ALL,
    )

    fun parse(htmlFooter: String): List<ContentBlock> {
        if (htmlFooter.isBlank()) return emptyList()

        val blocks = mutableListOf<ContentBlock>()

        for (match in videoRegex.findAll(htmlFooter)) {
            val platform = match.groupValues[1]
            val videoUrl = match.groupValues[2]
            val thumbnailUrl = urlNormalizer.normalize(match.groupValues[3])
            val title = match.groupValues[4].ifEmpty { "" }

            blocks.add(
                ContentBlock.Video(
                    thumbnailUrl = thumbnailUrl,
                    videoUrl = videoUrl,
                    title = title,
                    platform = platform,
                )
            )
        }

        return blocks
    }
}
