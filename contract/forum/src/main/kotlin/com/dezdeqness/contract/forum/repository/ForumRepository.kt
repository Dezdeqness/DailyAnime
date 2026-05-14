package com.dezdeqness.contract.forum.repository

import com.dezdeqness.contract.forum.model.ForumEntity

interface ForumRepository {

    fun getForums(): Result<List<ForumEntity>>
}
