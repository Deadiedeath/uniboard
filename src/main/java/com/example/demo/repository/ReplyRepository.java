package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.Board;
import com.example.demo.domain.Reply;
import com.example.demo.domain.User;
import com.example.demo.dto.replyDto;
import com.example.demo.repository.search.replySearch;
import com.example.demo.repository.search.search;



public interface ReplyRepository extends JpaRepository<Reply, Integer>, replySearch{

	List<Reply> findByNum(int num);
	
}
