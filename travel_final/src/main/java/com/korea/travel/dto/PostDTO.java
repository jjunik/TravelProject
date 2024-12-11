package com.korea.travel.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
	
	private Long postId;				//고유 id
	private String postTitle;		//게시글제목
	private String postContent;	//게시글내용
	private String userName;
	private List<String> placeList;
	private List<String> imageUrls;
	private String thumbnail;
	private int likes;
	private String postCreatedAt;	//게시글등록시간
	
}
