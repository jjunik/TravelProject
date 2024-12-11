package com.korea.travel.service;

import com.korea.travel.dto.PostDTO;
import com.korea.travel.model.PostEntity;
import com.korea.travel.persistence.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시판 전체 조회
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // 게시글 한 건 조회
    public PostDTO getPostById(Long id) {
    	Optional<PostEntity> board = postRepository.findById(id);
    	return board.map(this::convertToDTO).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // 게시글 생성
    public PostDTO createPost(PostDTO postDTO) {
        PostEntity savedEntity = postRepository.save(convertToEntity(postDTO));
        return convertToDTO(savedEntity);
    }
    
    // 게시글 수정
    public PostDTO updatePost(Long id, PostDTO postDTO) {
    	PostEntity updateEntity = postRepository
    			.findById(id)
    			.orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    	updateEntity.setPostTitle(postDTO.getPostTitle());
    	updateEntity.setPostContent(postDTO.getPostContent());
    	updateEntity.setPlaceList(postDTO.getPlaceList());
    	updateEntity.setImageUrls(postDTO.getImageUrls());
    	return convertToDTO(postRepository.save(updateEntity));
    	
    	
    }

    private PostDTO convertToDTO(PostEntity entity) {
        return PostDTO.builder()
                .postId(entity.getPostId())
                .postTitle(entity.getPostTitle())
                .postContent(entity.getPostContent())
                .userName(entity.getUserName())
                .placeList(entity.getPlaceList())
                .imageUrls(entity.getImageUrls())
                .thumbnail(entity.getThumbnail())
                .likes(entity.getLikes())
                .postCreatedAt(entity.getPostCreatedAt())
                .build();
    }

    private PostEntity convertToEntity(PostDTO dto) {
        return PostEntity.builder()
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .userName(dto.getUserName())
                .placeList(dto.getPlaceList())
                .imageUrls(dto.getImageUrls())
                .thumbnail(dto.getThumbnail())
                .likes(dto.getLikes())
                .postCreatedAt(dto.getPostCreatedAt())
                .build();
    }
}
