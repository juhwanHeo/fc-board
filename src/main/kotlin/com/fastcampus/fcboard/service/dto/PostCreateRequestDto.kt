package com.fastcampus.fcboard.service.dto

import com.fastcampus.fcboard.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequestDto.toEntity() = Post(
    title = this.title,
    content = this.content,
    createdBy = this.createdBy
)
