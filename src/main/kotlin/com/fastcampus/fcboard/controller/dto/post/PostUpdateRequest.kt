package com.fastcampus.fcboard.controller.dto.post

import com.fastcampus.fcboard.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
    val tags: List<String> = emptyList(),
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = this.title,
    content = this.content,
    updatedBy = this.updatedBy
)
