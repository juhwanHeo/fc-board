package com.fastcampus.fcboard.controller.dto.comment

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(
    content = content,
    updatedBy = updatedBy
)
