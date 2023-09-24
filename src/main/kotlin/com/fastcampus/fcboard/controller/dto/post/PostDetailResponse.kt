package com.fastcampus.fcboard.controller.dto.post

import com.fastcampus.fcboard.controller.dto.comment.CommentResponse
import com.fastcampus.fcboard.controller.dto.comment.toResponse
import com.fastcampus.fcboard.service.dto.PostDetailResponseDto

data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentResponse> = emptyList(),
    val tags: List<String> = emptyList(),
)

fun PostDetailResponseDto.toResponse() = PostDetailResponse(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt,
    comments = comments.map { it.toResponse() },
    tags = tags
)
