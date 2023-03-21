package com.example.demo.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class pageRequestDTO {

	@Builder.Default
	private int page = 1;
	
	@Builder.Default
	private int size = 10;
	
	@Builder.Default
	private String keyword ="";
	
	@Builder.Default
	private String type = "3";
	
	public Pageable getPageable(String...props) {
		return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
	}
	
	private String link;
	
	public String getLink() {
		if(link == null) {
			StringBuilder builder = new StringBuilder();
			builder.append("page=" + this.page);
			builder.append("&size=" + this.size);
			
			if(this.keyword != null) {
				builder.append("&keyword=" + this.keyword);
			}
			
			if(this.type != null) {
				builder.append("&type=" + this.type);
			}
			
			link = builder.toString();
		}
		return link;
		
	}
}
