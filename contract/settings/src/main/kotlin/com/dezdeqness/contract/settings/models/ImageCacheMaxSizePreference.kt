package com.dezdeqness.contract.settings.models

import com.dezdeqness.contract.settings.core.SettingsPreference
import com.dezdeqness.contract.settings.core.handlers.IntHandler

data object ImageCacheMaxSizePreference : SettingsPreference<Int> {
    override val name = "imageCacheMaxSize"
    override val default = FileSize.Mb512.size
    override val handler = IntHandler
}

enum class FileSize(val size: Int) {
    Mb128(128),
    Mb256(256),
    Mb512(512),
    Gb1(1024),
    Gb2(2048),
    Gb4(4096),
    Gb8(8192),
}
