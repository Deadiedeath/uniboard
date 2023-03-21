package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rnum;
	
	@Column(nullable = false)
	private int num;
	
	@Column(nullable = false)
	private String content;
	
	@Column(nullable = false)
	private String id;
	
	@Column
	private String msg;
	
	public void delete(String msg) {
		this.msg = msg;
	}
	
}
