package com.jaefan.munpyspring.shelter.domain.repository;

import static com.jaefan.munpyspring.region.domain.model.QRegion.*;
import static com.jaefan.munpyspring.shelter.domain.model.QShelter.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShelterRepositoryImpl implements ShelterRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Shelter> findAll() {
		return queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.fetch();
	}

	@Override
	public List<Shelter> findAllWithPagination(Pageable pageable) {
		List<Shelter> shelters = queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(shelter.count())
			.from(shelter);

		Page<Shelter> page = PageableExecutionUtils.getPage(shelters, pageable, countQuery::fetchOne);

		return page.getContent();
	}

	@Override
	public List<Shelter> findByRegion(String upper, List<String> lowers) {
		return queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.where(upperEq(upper), lowerIn(lowers))
			.fetch();
	}

	@Override
	public List<Shelter> findByRegionWithPagination(String upper, List<String> lowers, Pageable pageable) {
		List<Shelter> shelters = queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.where(upperEq(upper), lowerIn(lowers))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(shelter.count())
			.from(shelter)
			.where(upperEq(upper), lowerIn(lowers));

		Page<Shelter> page = PageableExecutionUtils.getPage(shelters, pageable, countQuery::fetchOne);

		return page.getContent();
	}

	private BooleanExpression upperEq(String upper) {
		return upper != null ? region.upper.eq(upper) : null;
	}

	private BooleanExpression lowerIn(List<String> lowers) {
		return lowers != null ? region.lower.in(lowers) : null;
	}
}
