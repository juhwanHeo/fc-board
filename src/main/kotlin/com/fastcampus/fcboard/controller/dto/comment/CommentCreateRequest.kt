package com.fastcampus.fcboard.controller.dto.comment

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
