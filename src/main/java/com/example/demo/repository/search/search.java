package com.example.demo.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.Board;

public interface search {

	Page<Board> searchBoard(Pageable pageable, String type, String keyword);

	
}
