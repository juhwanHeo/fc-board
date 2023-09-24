package com.fastcampus.fcboard.controller

import com.fastcampus.fcboard.controller.dto.post.PostCreateRequest
import com.fastcampus.fcboard.controller.dto.post.PostDetailResponse
import com.fastcampus.fcboard.controller.dto.post.PostSearchRequest
import com.fastcampus.fcboard.controller.dto.post.PostSummaryResponse
import com.fastcampus.fcboard.controller.dto.post.PostUpdateRequest
import com.fastcampus.fcboard.controller.dto.post.toDto
import com.fastcampus.fcboard.controller.dto.post.toResponse
import com.fastcampus.fcboard.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    @GetMapping
    fun findAllPost(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        println("tag: ${postSearchRequest.tag}")
        return postService.findPageBy(pageable, postSearchRequest.toDto()).toResponse()
    }

    @GetMapping("/{id}")
    fun findPost(
        @PathVariable id: Long,
    ): PostDetailResponse {
        return postService.getPost(id).toResponse()
    }

    @PostMapping
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): Long {
        println("tags: ${postCreateRequest.tags}")
        return postService.createPost(postCreateRequest.toDto())
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ): Long {
        println("tags: ${postUpdateRequest.tags}")
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
