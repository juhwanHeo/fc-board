package com.fastcampus.fcboard.controller.dto.post

import com.fastcampus.fcboard.service.dto.PostSearchRequestDto
import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam val title: String?,
    @RequestParam val createdBy: String?,
    @RequestParam val tag: String?,
)

fun PostSearchRequest.toDto() = PostSearchRequestDto(
    title = title,
    createdBy = createdBy
)
