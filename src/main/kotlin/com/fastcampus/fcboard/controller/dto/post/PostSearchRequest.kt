package com.fastcampus.fcboard.controller.dto.post

import com.fastcampus.fcboard.service.dto.PostSearchRequestDto

data class PostSearchRequest(
    val title: String?,
    val createdBy: String?,
)

fun PostSearchRequest.toDto() = PostSearchRequestDto(
    title = title,
    createdBy = createdBy
)
