package com.fastcampus.fcboard.service

import com.fastcampus.fcboard.domain.Comment
import com.fastcampus.fcboard.domain.Post
import com.fastcampus.fcboard.domain.Tag
import com.fastcampus.fcboard.exception.PostNotDeletableException
import com.fastcampus.fcboard.exception.PostNotFoundException
import com.fastcampus.fcboard.exception.PostNotUpdatableException
import com.fastcampus.fcboard.repository.CommentRepository
import com.fastcampus.fcboard.repository.PostRepository
import com.fastcampus.fcboard.repository.TagRepository
import com.fastcampus.fcboard.service.dto.PostCreateRequestDto
import com.fastcampus.fcboard.service.dto.PostSearchRequestDto
import com.fastcampus.fcboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
    private val likeService: LikeService,
) : BehaviorSpec({
    val redisContainer = GenericContainer<Nothing>("redis:5.0.3-alpine")
    afterSpec {
        redisContainer.stop()
    }
    beforeSpec {
        redisContainer.portBindings.add("16379:6379")
        redisContainer.start()
        listener(redisContainer.perSpec())
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "juhwan1", tags = listOf("tag1", "tag2")),
                Post(title = "title12", content = "content1", createdBy = "juhwan1", tags = listOf("tag1", "tag2")),
                Post(title = "title13", content = "content1", createdBy = "juhwan1", tags = listOf("tag1", "tag2")),
                Post(title = "title14", content = "content1", createdBy = "juhwan1", tags = listOf("tag1", "tag2")),
                Post(title = "title15", content = "content1", createdBy = "juhwan1", tags = listOf("tag1", "tag2")),
                Post(title = "title6", content = "content1", createdBy = "juhwan2", tags = listOf("tag1", "tag5")),
                Post(title = "title7", content = "content1", createdBy = "juhwan2", tags = listOf("tag1", "tag5")),
                Post(title = "title8", content = "content1", createdBy = "juhwan2", tags = listOf("tag1", "tag5")),
                Post(title = "title9", content = "content1", createdBy = "juhwan2", tags = listOf("tag1", "tag5")),
                Post(title = "title10", content = "content1", createdBy = "juhwan2", tags = listOf("tag1", "tag5"))
            )
        )
    }
    given("게시글 생성시") {
        When("정상적인 input") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "juhwan"
                )
            )

            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null

                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "juhwan"
            }
        }

        When("태그 추가 시") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "juhwan",
                    tags = listOf("tag1", "tag2")
                )
            )
            then("태그가 정상적으로 추가됨을 확인") {
                val tags = tagRepository.findByPostId(postId)
                tags.size shouldBe 2
                tags[0].name shouldBe "tag1"
                tags[1].name shouldBe "tag2"
            }
        }
    }
    given("게시글 수정시") {
        val saved = postRepository.save(
            Post(title = "title", content = "content", createdBy = "juhwan", tags = listOf("tag1", "tag2"))
        )
        When("정상적인 수정") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "juhwan"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다는 예외가 발생") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "juhwan"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시물 입니다. 예외가 발생한다.;") {
                val notUpdatableDto = PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "update juhwan"
                )
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(1L, notUpdatableDto)
                }
            }
        }
        When("태그가 수정되었을 때") {
            then("정상적으로 수정됨을 확인") {

                val updatedId = postService.updatePost(
                    saved.id,
                    PostUpdateRequestDto(
                        title = "update title",
                        content = "update content",
                        updatedBy = "juhwan",
                        tags = listOf("tag1", "tag2", "tag3")
                    )
                )
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag3"
            }
            then("태그 순서가 변경되었을때, 정상적으로 변경됨을 확인") {
                val updatedId = postService.updatePost(
                    saved.id,
                    PostUpdateRequestDto(
                        title = "update title",
                        content = "update content",
                        updatedBy = "juhwan",
                        tags = listOf("tag3", "tag2", "tag1")
                    )
                )

                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[0].name shouldBe "tag3"
                tags[1].name shouldBe "tag2"
                tags[2].name shouldBe "tag1"
            }
        }
    }
    given("게시글 삭제시") {
        When("정상 삭제시") {
            val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "juhwan"))
            val postId = postService.deletePost(saved.id, "juhwan")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }

        When("작성자가 동일하지 않으면") {
            val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "juhwan"))
            then("삭제할 수 없는 게시물 입니다. 예외가 발생한다.") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved.id, "juhwan2")
                }
            }
        }
    }

    given("게시글 상세조회시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "juhwan"))
        tagRepository.saveAll(
            listOf(
                Tag(name = "tag1", post = saved, createdBy = saved.createdBy),
                Tag(name = "tag2", post = saved, createdBy = saved.createdBy),
                Tag(name = "tag3", post = saved, createdBy = saved.createdBy)
            )
        )
        likeService.createLike(saved.id, "juhwan1")
        likeService.createLike(saved.id, "juhwan2")
        likeService.createLike(saved.id, "juhwan3")
        When("정상 조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환됨을 확인한다.") {
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "juhwan"
            }
            then("태그가 정상적으로 조회됨을 확인") {
                post.tags.size shouldBe 3
                post.tags[0] shouldBe "tag1"
                post.tags[1] shouldBe "tag2"
                post.tags[2] shouldBe "tag3"
            }
            then("좋아요 개수가 조회됨을 확인한다.") {
                post.likeCount shouldBe 3
            }
        }

        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다. 라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> { postService.getPost(9999L) }
            }
        }
        When("댓글 추가 시") {
            commentRepository.save(Comment(content = "댓글 내용1", post = saved, createdBy = "juhwan"))
            commentRepository.save(Comment(content = "댓글 내용2", post = saved, createdBy = "juhwan"))
            commentRepository.save(Comment(content = "댓글 내용3", post = saved, createdBy = "juhwan"))
            val post = postService.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인한다.") {
                post.comments.size shouldBe 3
                /* content */
                post.comments[0].content shouldBe "댓글 내용1"
                post.comments[1].content shouldBe "댓글 내용2"
                post.comments[2].content shouldBe "댓글 내용3"
                /* createdBy */
                post.comments[0].createdBy shouldBe "juhwan"
                post.comments[1].createdBy shouldBe "juhwan"
                post.comments[2].createdBy shouldBe "juhwan"
            }
        }
    }

    given("게시글 목록 조회시") {
        When("정상 조회시") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto())
            then("게시글 페이지가 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "juhwan"
            }
        }

        When("타이틀로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(title = "title1"))
            then("타이틀에 해당하는 게시글을 반환한다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "juhwan"
            }
        }

        When("작성자로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(createdBy = "juhwan1"))
            then("작성자에 해당하는 게시글을 반환한다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldBe "juhwan1"
            }
            then("첫번째 태그가 함께 조회됨을 확인") {
                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }
        When("태그로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            then("태그에 해당하는 게시글을 반환") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldBe "title10"
                postPage.content[1].title shouldBe "title9"
                postPage.content[2].title shouldBe "title8"
                postPage.content[3].title shouldBe "title7"
                postPage.content[4].title shouldBe "title6"
            }
        }
        When("좋아요가 2개 추가되었을 때") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            postPage.content.forEach {
                likeService.createLike(it.id, "juhwan1")
                likeService.createLike(it.id, "juhwan2")
            }

            val likedPostPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDto(tag = "tag5"))
            then("좋아요 개수가 정상적으로 조회됨을 확인한다.") {
                likedPostPage.content.forEach {
                    it.likeCount shouldBe 2
                }
            }
        }
    }
})
