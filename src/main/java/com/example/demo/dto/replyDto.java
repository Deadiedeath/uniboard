package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class replyDto {

	
	private int rnum;
	
	@NotBlank(message = "제목을 입력하세요")
	private int num;
	
	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
	private String id;
	
	private String msg;
	
	private long count;
}
