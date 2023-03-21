package com.example.demo.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import com.example.demo.dto.boardDto;
import com.example.demo.dto.pageRequestDTO;
import com.example.demo.dto.pageResponseDTO;
import com.example.demo.dto.replyDto;

public interface BoardService {

	List<Board> findall(); //전체 게시물 조회
	
	boardDto findbynum(int num); //게시물 번호로 데이터 조회
	
	boardDto posting(boardDto dto);//작성한 게시물 저장
	
	String deletepost(int num);//게시물 번호로 데이터 삭제
	
	String updatepost(boardDto dto);//수정한 데이터 저장
	
	pageResponseDTO<boardDto> list(pageRequestDTO pageRequestDTO); //조건에 따른 게시물 페이징
	
	void addReply(replyDto dto); //댓글 저장
	
	void deleteReply(int rnum, String msg); //댓글 삭제
	
	Reply findbyrnum(int rnum); //댓글 검색
	
	List<replyDto> replyCount(); //게시물 댓글 수 검색
	
}
