package com.fastcampus.fcboard.service

import com.fastcampus.fcboard.controller.dto.comment.CommentCreateRequestDto
import com.fastcampus.fcboard.controller.dto.comment.CommentUpdateRequestDto
import com.fastcampus.fcboard.domain.Comment
import com.fastcampus.fcboard.domain.Post
import com.fastcampus.fcboard.exception.CommentNotDeletableException
import com.fastcampus.fcboard.exception.CommentNotUpdatableException
import com.fastcampus.fcboard.exception.PostNotFoundException
import com.fastcampus.fcboard.repository.CommentRepository
import com.fastcampus.fcboard.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("댓글 생성 시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "juhwan"
            )
        )
        val createRequestDto = CommentCreateRequestDto(
            content = "댓글 내용",
            createdBy = "juhwan"
        )
        When("정상적인 input 이면") {
            val commentId = commentService.createComment(post.id, createRequestDto)
            then("Success Created") {
                commentId shouldBeGreaterThan 0L
                val comment = commentRepository.findByIdOrNull(commentId)
                comment shouldNotBe null
                comment?.content shouldBe "댓글 내용"
                comment?.createdBy shouldBe "juhwan"
            }
        }

        When("게시글이 존재 하지 않으면") {
            then("Post 404 Error") {
                shouldThrow<PostNotFoundException> { commentService.createComment(9999L, createRequestDto) }
            }
        }
    }
    given("댓글 수정 시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "juhwan"
            )
        )
        val saved = commentRepository.save(
            Comment(
                content = "댓글 내용",
                createdBy = "juhwan",
                post = post
            )
        )
        When("정상적인 input 이면") {
            val updateId = commentService.updateComment(
                saved.id,
                CommentUpdateRequestDto(
                    content = "수정된 댓글 내용",
                    updatedBy = "juhwan"
                )
            )
            then("Success Updated") {
                updateId shouldBe saved.id
                val updated = commentRepository.findByIdOrNull(updateId)
                updated shouldNotBe null
                updated?.content shouldBe "수정된 댓글 내용"
                updated?.updatedBy shouldBe "juhwan"
            }
        }
        When("작성자와 수정자가 다르면") {
            then("수정할 수 없는 댓글 예외 발생") {
                shouldThrow<CommentNotUpdatableException> {
                    commentService.updateComment(
                        saved.id,
                        CommentUpdateRequestDto(
                            content = "수정된 댓글 내용",
                            updatedBy = "update juhwan"
                        )
                    )
                }
            }
        }
    }
    given("댓글 삭적 시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "juhwan"
            )
        )

        val saved1 = commentRepository.save(
            Comment(
                content = "댓글 내용1",
                createdBy = "juhwan",
                post = post
            )
        )

        val saved2 = commentRepository.save(
            Comment(
                content = "댓글 내용2",
                createdBy = "juhwan",
                post = post
            )
        )

        When("정상적인 input 이면") {
            val deleteId = commentService.deleteComment(saved1.id, "juhwan")
            then("Success Deleted") {
                deleteId shouldBe saved1.id
                commentRepository.findByIdOrNull(deleteId) shouldBe null
            }
        }
        When("작성자와 삭제자가 다르면") {
            then("삭제할 수 없는 댓글 예외가 발생") {
                shouldThrow<CommentNotDeletableException> {
                    commentService.deleteComment(saved2.id, "삭제자")
                }
            }
        }
    }
})
