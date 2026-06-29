package com.dayou.postory.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dayou.postory.api.dto.request.CommentRequest;
import com.dayou.postory.api.dto.request.PostRequest;
import com.dayou.postory.api.dto.response.PostDetailResponse;
import com.dayou.postory.api.dto.response.PostResponse;
import com.dayou.postory.global.annotation.LoginUser;
import com.dayou.postory.global.response.GlobalResponse;
import com.dayou.postory.service.CommentService;
import com.dayou.postory.service.LikeService;
import com.dayou.postory.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApiController {
	private final PostService postService;
	private final CommentService commentService;
	private final LikeService likeService;

	@PostMapping("/posts")
	public ResponseEntity<GlobalResponse<Void>> publishPost(@LoginUser Long userId,
		@Valid @RequestBody PostRequest postRequest) {
		postService.publishedPost(userId, postRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponse.success());
	}

	@GetMapping("/posts")
	public ResponseEntity<GlobalResponse<List<PostResponse>>> getPosts() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success(postService.getPosts()));
	}

	@GetMapping("/improved-posts")
	public ResponseEntity<GlobalResponse<Page<PostResponse>>> improvedGetPosts(
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success(postService.improvedGetPosts(page, size)));
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<GlobalResponse<PostDetailResponse>> getPost(@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success(postService.getPostById(postId)));
	}

	@PatchMapping("/posts/{postId}")
	public ResponseEntity<GlobalResponse<Void>> updatePost(@LoginUser Long userId, @PathVariable Long postId,
		@Valid @RequestBody PostRequest postRequest) {
		postService.updatePost(userId, postId, postRequest);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<GlobalResponse<Void>> deletePost(@LoginUser Long userId, @PathVariable Long postId) {
		postService.deletePost(userId, postId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<GlobalResponse<Void>> createComment(@LoginUser Long userId, @PathVariable Long postId,
		@Valid @RequestBody CommentRequest commentRequest) {
		commentService.createComment(userId, postId, commentRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponse.success());
	}

	@PatchMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<GlobalResponse<Void>> updateComment(@LoginUser Long userId, @PathVariable Long postId,
		@PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest) {
		commentService.updateComment(userId, postId, commentId, commentRequest);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}

	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<GlobalResponse<Void>> deleteComment(@LoginUser Long userId, @PathVariable Long postId,
		@PathVariable Long commentId) {
		commentService.deleteComment(userId, postId, commentId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}

	@PostMapping("/posts/{postId}/likes")
	public ResponseEntity<GlobalResponse<Void>> likePost(@LoginUser Long userId, @PathVariable Long postId) {
		likeService.likePost(userId, postId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}

	@DeleteMapping("/posts/{postId}/likes")
	public ResponseEntity<GlobalResponse<Void>> unLikePost(@LoginUser Long userId, @PathVariable Long postId) {
		likeService.unlikePost(userId, postId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success());
	}
}
