package com.example.demo.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.Board;
import com.example.demo.domain.User;

public interface usersearch {

	Page<User> searchUser(Pageable pageable, int level, String keyword);
}
