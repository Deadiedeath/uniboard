package com.example.demo.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class boardDto {

	
	private int num;
	
	@NotBlank(message = "제목을 입력하세요")
	private String title;
	
	@NotBlank(message = "내용을 입력하세요")
	private String content;
	
	private String id;
	
	private String msg;
	
	private List<replyDto> reply;
	
	public void addreply(List<replyDto> dto) {
		this.reply = dto;
	}
	
}
