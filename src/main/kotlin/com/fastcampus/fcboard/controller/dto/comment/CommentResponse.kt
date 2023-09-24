package com.fastcampus.fcboard.controller.dto.comment

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)
