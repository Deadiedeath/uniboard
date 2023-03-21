package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class userinfo {
	
	private String id;
	
	private int level;
	
	private boolean check; //로그인 성공시 true / 로그인 실패시 false(비밀번호, 아이디가 틀렸을경우)

}
