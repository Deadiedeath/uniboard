package com.example.demo.repository.search;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.demo.domain.Board;
import com.example.demo.domain.QBoard;
import com.example.demo.domain.QUser;
import com.example.demo.domain.User;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class usersearchImpl extends QuerydslRepositorySupport implements usersearch{

	private JPAQueryFactory queryFactory;
	
	public usersearchImpl(EntityManager em) {
		super(User.class);
		this.queryFactory = new JPAQueryFactory(em);
	}


	@Override
	public Page<User> searchUser(Pageable pageable, int level, String keyword) {
		
		QUser user =QUser.user;
		
		JPQLQuery<User> query = from(user);
		
		if(level != 3) { //조건이 있을경우
			query.where(user.level.eq(level));
		}
		
		if(keyword != null && keyword != "") { //검색어가 있을경우

			query.where(user.id.contains(keyword));

		}
		
		this.getQuerydsl().applyPagination(pageable, query);
		
		List<User> list = query.fetch();
		
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}

}
