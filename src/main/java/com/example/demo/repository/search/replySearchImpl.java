package com.example.demo.repository.search;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.demo.domain.Board;
import com.example.demo.domain.QBoard;
import com.example.demo.domain.QReply;
import com.example.demo.domain.Reply;
import com.example.demo.dto.replyDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class replySearchImpl extends QuerydslRepositorySupport implements replySearch{

private JPAQueryFactory queryFactory;
	
	public replySearchImpl(EntityManager em) {
		super(Reply.class);
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<replyDto> replyCount() {
		//select b.num as num, count(r.rnum) from board b left join reply r on b.num =r.num group by b.num order by b.num desc; 쿼리 기반 
		
		QReply reply = QReply.reply;
		QBoard board = QBoard.board;
		
		JPQLQuery<Board> query = from(board);
		query.leftJoin(reply).on(board.num.eq(reply.num));
		
		query.groupBy(board.num);
		
		JPQLQuery<replyDto> dtoQuery = query.select(
				Projections.bean(replyDto.class, 
						board.num,
						reply.count().as("count")
				)
				
				).orderBy(board.num.desc());
		
//		JPQLQuery<replyDto> dtoQuery = queryFactory.from(reply)
//				.groupBy(reply.num)
//				.select(
//						Projections.bean(replyDto.class, reply.num,
//								reply.count().as("count")
//								)
//						);
		
		
		List<replyDto> replyCount = dtoQuery.fetch();
		return replyCount;
	}
	
	
}
