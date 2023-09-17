package com.fastcampus.fcboard.service.dto

import com.fastcampus.fcboard.domain.Post

data class PostDetailResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Post.toDetailResponseDto() = PostDetailResponseDto(
    id = this.id,
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt.toString()
)
