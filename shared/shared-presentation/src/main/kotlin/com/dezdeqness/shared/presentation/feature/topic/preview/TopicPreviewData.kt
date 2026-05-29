package com.dezdeqness.shared.presentation.feature.topic.preview

import com.dezdeqness.shared.presentation.feature.topic.model.ContentBlock
import com.dezdeqness.shared.presentation.feature.topic.model.ParagraphBlock
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

object TopicPreviewData {

    val reviewTopic = TopicPresentationModel(
        topicId = 1,
        title = "Обзор весеннего сезона 2024",
        userNickname = "AnimeReviewer",
        userAvatarUrl = "",
        date = "2 часа назад",
        commentsCount = 42,
        contentBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Новый сезон радует разнообразием жанров."),
                    ContentBlock.Text("Особенно выделяются", bold = true),
                    ContentBlock.Link(text = "Solo Leveling", url = ""),
                    ContentBlock.Text("и"),
                    ContentBlock.Link(text = "Oshi no Ko 2", url = ""),
                ),
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Полный список тайтлов сезона с оценками внутри.", italic = true),
                ),
            ),
        ),
        footerBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("42 комментария", bold = true),
                ),
            ),
        ),
    )

    val pollTopic = TopicPresentationModel(
        topicId = 2,
        title = "Лучшие опенинги сезона — голосование",
        userNickname = "MusicFan",
        userAvatarUrl = "",
        date = "5 часов назад",
        commentsCount = 18,
        contentBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Голосуем за лучший опенинг!", bold = true, underline = true),
                ),
            ),
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Кандидаты: OP от"),
                    ContentBlock.Link(text = "YOASOBI", url = ""),
                    ContentBlock.Text(", OP от"),
                    ContentBlock.Link(text = "Kenshi Yonezu", url = ""),
                    ContentBlock.Text("и новый трек"),
                    ContentBlock.Text("LiSA.", bold = true),
                ),
            ),
        ),
        footerBlocks = emptyList(),
    )

    val newsTopic = TopicPresentationModel(
        topicId = 3,
        title = "Новости: анонсы на осенний сезон",
        userNickname = "NewsBot",
        userAvatarUrl = "",
        date = "Вчера",
        commentsCount = 95,
        contentBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Студия"),
                    ContentBlock.Link(text = "MAPPA", url = ""),
                    ContentBlock.Text("анонсировала"),
                    ContentBlock.Text("3 новых проекта", bold = true),
                    ContentBlock.Text("на осень. Среди них — долгожданная адаптация"),
                    ContentBlock.Text("Dandadan", italic = true),
                    ContentBlock.Text("и продолжение"),
                    ContentBlock.Link(text = "Jujutsu Kaisen", url = ""),
                ),
            ),
        ),
        footerBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Link(text = "Читать полностью →", url = ""),
                ),
            ),
        ),
    )

    val guideTopic = TopicPresentationModel(
        topicId = 101,
        title = "Как начать читать мангу?",
        userNickname = "MangaFan",
        userAvatarUrl = "",
        date = "5 часов назад",
        commentsCount = 23,
        contentBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Гайд для новичков.", bold = true),
                    ContentBlock.Text("Собрал список лучших манг для старта. Делитесь рекомендациями!"),
                ),
            ),
        ),
        footerBlocks = emptyList(),
    )

    val detailedTopic = TopicPresentationModel(
        topicId = 100,
        title = "Атака титанов: Финал — обсуждение финальной серии",
        userNickname = "TitanSlayer",
        userAvatarUrl = "",
        date = "3 часа назад",
        commentsCount = 156,
        contentBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text(
                        text = "Наконец-то вышла финальная серия!",
                        bold = true,
                    ),
                ),
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Давайте обсудим концовку. Как вам финал? Студия"),
                    ContentBlock.Link(text = "MAPPA", url = "https://shikimori.one/studios/569"),
                    ContentBlock.Text("отлично справилась с адаптацией манги"),
                    ContentBlock.Link(text = "Хадзимэ Исаямы", url = ""),
                    ContentBlock.Text("— финальная арка получилась"),
                    ContentBlock.Text("эмоциональной и масштабной.", italic = true),
                ),
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.QuoteContent(
                blocks = listOf(
                    ParagraphBlock.InlineContent(
                        blocks = listOf(
                            ContentBlock.Text(
                                "Анимация на высшем уровне, саундтрек идеально подобран. Финальный бой — лучшее, что я видел за последние годы.",
                                italic = true,
                            ),
                        ),
                    ),
                    ParagraphBlock.InlineContent(
                        blocks = listOf(
                            ContentBlock.Text("— @SakuraViewer", bold = true),
                        ),
                    ),
                ),
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Ключевые моменты:", bold = true, underline = true),
                ),
            ),
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("• Анимация финального боя"),
                    ContentBlock.LineBreak,
                    ContentBlock.Text("• Саундтрек от"),
                    ContentBlock.Link(text = "Хироюки Савано", url = ""),
                    ContentBlock.LineBreak,
                    ContentBlock.Text("• Концовка, отличающаяся от манги"),
                ),
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.ImageContent(
                previewUrl = "",
                originalUrl = "",
            ),
            ParagraphBlock.Spacer,
            ParagraphBlock.VideoContent(
                thumbnailUrl = "",
                videoUrl = "https://youtube.com/watch?v=example",
                title = "Финальный трейлер",
            ),
        ),
        footerBlocks = listOf(
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Моя оценка:", bold = true),
                    ContentBlock.Text("9/10"),
                ),
            ),
            ParagraphBlock.InlineContent(
                blocks = listOf(
                    ContentBlock.Text("Обсуждение на форуме:", italic = true),
                    ContentBlock.Link(text = "перейти в тему", url = ""),
                ),
            ),
        ),
        linkedType = "Anime",
        linkedId = 16498,
        linkedTitle = "Атака титанов: Финал",
        linkedImageUrl = "",
    )

    val topicListItems = listOf(reviewTopic, pollTopic, newsTopic)

    val hotTopics = listOf(reviewTopic, guideTopic)
}
