package com.korea.travel.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.korea.travel.dto.UserDTO;
import com.korea.travel.model.UserEntity;
import com.korea.travel.persistence.UserRepository;
import com.korea.travel.security.TokenProvider;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository repository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final TokenProvider tokenProvider;
	
	
	//모든유저 조회
	public List<UserDTO> getAllUsersId() {
		
		List<UserEntity> allUsersId = repository.findAll();
		
		return allUsersId.stream().map(UserDTO::new).collect(Collectors.toList());
		
	}
	
	
	//userId가 있는지 중복체크
	public boolean getUserIds(String userId) {
		
		UserEntity entity = repository.findByUserId(userId);
		//중복되는 userId가 없으면 true
		if(entity == null) {
			return true;
		}else{
			return false;
		}
		
	}
	
	
	//회원가입
	public UserDTO signup(UserDTO dto) {
		
		UserEntity user = UserEntity.builder()
				.userId(dto.getUserId())
				.userName(dto.getUserName())
				.userNickName(dto.getUserNickName())
				.userPassword(passwordEncoder.encode(dto.getUserPassword()))
				.userCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
				.build();
		
		if(user == null || user.getUserId() == null) {
			throw new RuntimeException("Invalid Arguments 유효하지 않은 인자");
		} 
		final String userId = user.getUserId();
		//존재하는 ID인지 검사
		if(repository.existsByUserId(userId)) {
			log.warn("userId이 이미 존재 합니다.1 {}", userId);
			throw new RuntimeException("이미 존재하는 ID 입니다.");
		}else {
			repository.save(user);
			return UserDTO.builder()
					.userId(user.getUserId())
					.userName(user.getUserName())
					.userNickName(user.getUserNickName())
					.userPassword(user.getUserPassword())
					.build();
		}
		
	}
	
	
	//로그인(로그인할때 토큰생성)
	public UserDTO getByCredentials(String userId,String userPassword) {
		
		UserEntity user = repository.findByUserId(userId);		
		
		//user가 존재하면 /DB에 저장된 암호화된 비밀번호와 사용자에게 입력받아 전달된 암호화된 비밀번호를 비교
		if(user != null && passwordEncoder.matches(userPassword,user.getUserPassword())) {
			//토큰생성(180분설정해둠)
			final String token = tokenProvider.create(user);
			
			user.setToken(token);
			
			return UserDTO.builder()
				.id(user.getId())
				.userId(user.getUserId())
				.userName(user.getUserName())
				.userNickName(user.getUserNickName())
				.userPassword(user.getUserPassword())
				.userProfileImage(user.getUserProfileImage())
				.token(user.getToken())
				.build();
		}else {
			return null;
		}
		
	}
	
	
	//userPassword 수정하기
	public boolean userPasswordEdit (Long id,UserDTO dto) {
		
		Optional <UserEntity> user = repository.findById(id);
		
		if(user.isPresent()) {
						
			//비밀번호 확인후 변경
			if(!passwordEncoder.matches(dto.getUserPassword(),user.get().getUserPassword())) {
				
				UserEntity entity = user.get();
				entity.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
				repository.save(entity);
				return true;
				
			}else {
				System.out.println("변경하려는 비밀번호가 기존 비밀번호랑 똑같다");
				return false;
			}
		} else {
			return false;
		}
		
	}
		
	
		
	//userNickName 수정하기
    public boolean userNickNameEdit(Long id,UserDTO dto) {
    	
    	Optional <UserEntity> user = repository.findById(id);
    	
    	//유저 확인
    	if(user.isPresent() && user.get().getUserNickName() != dto.getUserNickName()) {    		
    		UserEntity entity = user.get();    		
			//변경된 userNickName 저장
			entity.setUserNickName(dto.getUserNickName());
    		repository.save(entity);
    		return true;
		} else {
			return false;
		}    	
    	
    }
    
    
    //프로필사진 수정
    @Transactional
    public UserDTO userProfileImageEdit(Long id, MultipartFile file) {
    	
        try {
            // 1. ID로 사용자 정보 확인 (UserEntity 찾기)
            UserEntity userEntity = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            // 2. 파일 경로 설정 및 저장 처리
            String uploadDir = System.getProperty("user.dir") + "/uploads/profilePictures/";
            String fileName = file.getOriginalFilename().replaceAll("[\\s\\(\\)]", "_");
            String filePath = uploadDir + id + "_" + fileName;
                        
            File dest = new File(filePath);            
            File parentDir = dest.getParentFile();
            if (!parentDir.exists()) {
            	parentDir.mkdirs();  // 디렉토리 생성
            }
            
            
            try {
                file.transferTo(dest);
                System.out.println("파일저장완료");
            } catch (IOException e) {
                System.err.println("파일 저장 실패: " + e.getMessage());
                e.printStackTrace();  // 스택 트레이스 출력
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
            
            String fileUrl = "http://localhost:9090/uploads/profilePictures/" + id + "_" + fileName;
            
            // 3. UserEntity에 프로필 사진 경로 업데이트
            userEntity.setUserProfileImage(fileUrl);
            repository.save(userEntity);  // UserEntity 업데이트 저장
            
            // 4. 업데이트된 UserEntity를 UserDTO로 변환하여 반환
            return UserDTO.builder().
            		userProfileImage(userEntity.getUserProfileImage())
            		.build();
            
        } catch (IOException e) {
            // 파일 저장 오류 처리
            throw new RuntimeException("프로필 사진 업로드 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            // 다른 예외 처리
            throw new RuntimeException("프로필 사진 수정 중 오류가 발생했습니다.", e);
        }
    }

    
    //로그아웃
    public boolean logout (Long id) {
    	Optional<UserEntity> user = repository.findById(id);
    	
    	if(user.isPresent()) {
    		UserEntity.builder()
    			.token(null);
    		UserDTO.builder()
	    		.id(null)
				.userId(null)
				.userName(null)
				.userNickName(null)
				.userPassword(null)
				.userProfileImage(null)
				.token(null)
				.build();
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    
    
    //회원탈퇴
    public boolean userWithdrawal (Long id, UserDTO dto) {
    	
    	Optional<UserEntity> user = repository.findById(id);
    	if(user.isPresent() && passwordEncoder.matches(dto.getUserPassword(),user.get().getUserPassword())) {
    		String token = dto.getToken();
    		
    		if(!tokenProvider.isTokenExpired(token)) {
    			UserEntity entity = user.get();
        		repository.delete(entity);
        		return true;
    		}else {
    			return false;
    		}
    	}else {
			return false;
		}
    	
    }
    
    
}
