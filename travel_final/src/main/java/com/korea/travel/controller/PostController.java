package com.korea.travel.controller;

import com.korea.travel.dto.PostDTO;
import com.korea.travel.dto.ResponseDTO;
import com.korea.travel.model.PostEntity;
import com.korea.travel.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // React 앱이 동작하는 주소
public class PostController {

    @Autowired
    private PostService postService;

    // 게시판 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
    	List<PostDTO> dtos = postService.getAllPosts();
    	ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().data(dtos).build();
    	System.out.println(response);
        return ResponseEntity.ok(response);
    }
    
    // 게시글 한 건 조회 postDetail
    @GetMapping("/posts/postDetail/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
    	System.out.println("id : " + id);
    	List<PostDTO> dtos = Arrays.asList(postService.getPostById(id));
    	System.out.println("dtos : " + dtos);
    	ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().data(dtos).build();
    	return ResponseEntity.ok(response);
    }
    
    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) {
    	List<PostDTO> dtos = Arrays.asList(postService.createPost(postDTO));
    	ResponseDTO<PostDTO> response = ResponseDTO.<PostDTO>builder().data(dtos).build();
        return ResponseEntity.ok(response);
    }
    
    // 게시글 수정
    @PutMapping("/postEdit/{id}")
    public boolean updatePost(@PathVariable Long id , @RequestBody PostDTO postDTO){
    	PostDTO dto = postService.updatePost(id, postDTO);
    	if(dto != null) {
    		return true;
    	}
    	return false;
    }
    
    
    
   
}
