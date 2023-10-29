package com.fastcampus.fcboard.controller

import com.fastcampus.fcboard.utils.RedisUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/redis")
class RedisController(
    private val redisUtils: RedisUtils,
) {

    @GetMapping
    fun getRedisCount(): Long {
        val key = "count"
        redisUtils.increment(key)
        return redisUtils.getCount(key) ?: 0L
    }
}
