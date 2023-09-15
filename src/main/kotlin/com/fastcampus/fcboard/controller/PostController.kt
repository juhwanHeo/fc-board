package com.fastcampus.fcboard.controller

import com.fastcampus.fcboard.controller.dto.*
import com.fastcampus.fcboard.service.PostService

import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.print.Pageable
import java.time.LocalDateTime

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun findAllPost(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        return Page.empty()
    }

    @GetMapping("/{id}")
    fun findPost(
        @PathVariable id: Long,
    ): PostDetailResponse {
        return PostDetailResponse(1L, "title", "content", "createdBy", LocalDateTime.now().toString())
    }

    @PostMapping
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): Long {
        return postService.createPost(postCreateRequest.toDto())
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ): Long {
        return postService.updatePost(id, postUpdateRequest.toDto())
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long {
        return postService.deletePost(id, createdBy)
    }
}
