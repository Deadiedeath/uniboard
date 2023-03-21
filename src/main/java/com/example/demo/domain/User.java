package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

	@Id
	@Column(nullable = false)
	private String id;
	
	@Column(nullable = false)
	private String pwd;
	
	@Column(nullable = false)
	@Builder.Default
	private int level = 0;

	public void promote(int level) {
		this.level = level;
	}
}
