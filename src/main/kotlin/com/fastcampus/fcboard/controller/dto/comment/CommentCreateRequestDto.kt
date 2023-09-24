package com.fastcampus.fcboard.controller.dto.comment

import com.fastcampus.fcboard.domain.Comment
import com.fastcampus.fcboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
