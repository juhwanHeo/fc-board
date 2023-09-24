package com.fastcampus.fcboard.controller.dto.comment

data class CommentUpdateRequest(
    val content: String,
    val updateBy: String,
)
