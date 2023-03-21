package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.repository.search.search;
import com.example.demo.repository.search.usersearch;


public interface UserRepository extends JpaRepository<User, String>, usersearch{

	List<User> findBylevel(int level);
}
