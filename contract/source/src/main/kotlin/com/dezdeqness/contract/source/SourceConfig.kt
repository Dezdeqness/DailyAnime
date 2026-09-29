package com.dezdeqness.contract.source

interface SourceConfig {
    val type: SourceType
    val features: Set<SourceFeature>
}

inline fun <reified T : SourceFeature> SourceConfig.hasFeature(): Boolean = features.any { it is T }
