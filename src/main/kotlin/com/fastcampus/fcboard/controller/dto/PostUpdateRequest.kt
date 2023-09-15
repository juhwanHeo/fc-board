package com.fastcampus.fcboard.controller.dto

import com.fastcampus.fcboard.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = this.title,
    content = this.content,
    updatedBy = this.updatedBy
)
