package com.example.demo.repository.search;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.demo.domain.Board;
import com.example.demo.domain.QBoard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class searchImpl extends QuerydslRepositorySupport implements search{

	private JPAQueryFactory queryFactory;
	
	public searchImpl(EntityManager em) {
		super(Board.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Board> searchBoard(Pageable pageable, String type, String keyword) {
		QBoard board =QBoard.board;
		
		JPQLQuery<Board> query = from(board);
		
		query.where(board.num.gt(1));
		
		if(type != null && keyword != null) {
			if(type.equals("id")) {
				query.where(board.id.contains(keyword));
			}
			else if(type.equals("content")) {
				query.where(board.content.contains(keyword));
			}
			else if(type.equals("title")) {
				query.where(board.title.contains(keyword));
			}
		}
		
		this.getQuerydsl().applyPagination(pageable, query);
		
		List<Board> list = query.fetch();
		
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}



}
