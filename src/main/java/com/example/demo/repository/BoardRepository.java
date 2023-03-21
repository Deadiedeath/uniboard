package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Board;
import com.example.demo.repository.search.search;



public interface BoardRepository extends JpaRepository<Board, Integer>, search{

	
}
