package com.fastcampus.fcboard.event.handler

import com.fastcampus.fcboard.domain.Like
import com.fastcampus.fcboard.event.dto.LikeEvent
import com.fastcampus.fcboard.exception.PostNotFoundException
import com.fastcampus.fcboard.repository.LikeRepository
import com.fastcampus.fcboard.repository.PostRepository
import com.fastcampus.fcboard.utils.RedisUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LikeEventHandler(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtils: RedisUtils,
) {

    @Async
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        Thread.sleep(3000)
        println("Like Event $event")
        val post = postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException()
        redisUtils.increment(redisUtils.getLikeCountKey(event.postId))
        likeRepository.save(Like(post, event.createdBy))
    }
}
