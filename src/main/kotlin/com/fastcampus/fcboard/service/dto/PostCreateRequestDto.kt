package com.fastcampus.fcboard.service.dto

import com.fastcampus.fcboard.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequestDto.toEntity() = Post(
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
    tags = tags,
)
