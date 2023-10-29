package com.fastcampus.fcboard.service

import com.fastcampus.fcboard.event.dto.LikeEvent
import com.fastcampus.fcboard.repository.LikeRepository
import com.fastcampus.fcboard.repository.PostRepository
import com.fastcampus.fcboard.utils.RedisUtils
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtils: RedisUtils,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun createLike(postId: Long, createdBy: String) {
        applicationEventPublisher.publishEvent(LikeEvent(postId, createdBy))
    }

    fun countLike(postId: Long): Long {
        redisUtils.getCount(redisUtils.getLikeCountKey(postId))?.let { return it }

        with(likeRepository.countByPostId(postId)) {
            redisUtils.setData(redisUtils.getLikeCountKey(postId), this)
            return this
        }
    }
}
