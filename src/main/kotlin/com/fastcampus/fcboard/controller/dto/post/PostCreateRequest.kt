package com.fastcampus.fcboard.controller.dto.post

import com.fastcampus.fcboard.service.dto.PostCreateRequestDto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDto() = PostCreateRequestDto(
    title = this.title,
    content = this.content,
    createdBy = this.createdBy
)
