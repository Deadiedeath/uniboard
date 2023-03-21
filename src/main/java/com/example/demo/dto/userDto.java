package com.example.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class userDto {

	@NotBlank(message = "아이디를 입력하세요")
	@Size(min = 2, max = 10, message = "2자 이상 10자 이하로 입력하세요")
	private String id;
	
	@NotBlank(message = "아이디를 입력하세요")
	@Size(min = 3)
	private String pwd;
	
	private int level;
}
