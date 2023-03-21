package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.userDto;
import com.example.demo.dto.userinfo;

public interface UserService {

	boolean checkid(String id); //중복 id 값 검사
	
	userinfo checkuser(String id, String pwd); //로그인을 위해 id pwd 검사
	
	String joinuser(userDto dto); //회원 가입
	
	List<User> findbylevel(int level); // level 값으로 db에서 데이터 조회
	
	List<User> findbyAll();//전체 리스트 조회
	
	void promoteN(String id, int level);//id의 level을 전달 받은 값으로 수정
	
	void deleteUser(String id); //id값을 통해 db 데이터 삭제
	
	User findbyid(String id);//id 값으로 데이터 조회
	
	pageResponseDTO<userDto> list(pageRequestDTO pageRequestDTO); //level값으로 데이터를 조회하여 페이징 
}
