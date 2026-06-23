package com.dayou.postory.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dayou.postory.api.dto.request.PostRequest;
import com.dayou.postory.api.dto.response.PostResponse;
import com.dayou.postory.global.annotation.LoginUser;
import com.dayou.postory.global.response.GlobalResponse;
import com.dayou.postory.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApiController {
	private final PostService postService;

	@PostMapping("/posts")
	public ResponseEntity<GlobalResponse<PostResponse>> publishPost(@LoginUser Long userId,
		@RequestBody PostRequest postRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponse.success(postService.publishedPost(userId, postRequest)));
	}

	@GetMapping("/posts")
	public ResponseEntity<GlobalResponse<List<PostResponse>>> getPost() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success(postService.getPosts()));
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<GlobalResponse<PostResponse>> getPost(@PathVariable Long postId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponse.success(postService.getPostById(postId)));
	}

	@PatchMapping("/posts/{postId}")
	public ResponseEntity<GlobalResponse<Void>> updatePost(@LoginUser Long userId, @PathVariable Long postId,
		@RequestBody PostRequest postRequest) {
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
}
