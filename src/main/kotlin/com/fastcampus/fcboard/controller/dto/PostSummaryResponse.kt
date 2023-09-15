package com.fastcampus.fcboard.controller.dto

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
)
