package com.jaefan.munpyspring.shelter.domain.repository;

import static com.jaefan.munpyspring.region.domain.model.QRegion.*;
import static com.jaefan.munpyspring.shelter.domain.model.QShelter.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShelterRepositoryImpl implements ShelterRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Shelter> findByRegion(Map<String, String> regionMap) {
		return queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.where(containsUpper(regionMap), containsLower(regionMap))
			.fetch();
	}

	@Override
	public List<Shelter> findByRegionWithPagination(Map<String, String> regionMap, Pageable pageable) {
		List<Shelter> shelters = queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region).fetchJoin()
			.where(containsUpper(regionMap), containsLower(regionMap))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(shelter.count())
			.from(shelter)
			.where(containsUpper(regionMap), containsLower(regionMap));

		Page<Shelter> page = PageableExecutionUtils.getPage(shelters, pageable, countQuery::fetchOne);

		return page.getContent();
	}

	private BooleanExpression containsUpper(Map<String, String> regionMap) {
		Set<String> uppers = regionMap.keySet();

		return regionMap.isEmpty() ? Expressions.asBoolean(true).isTrue() :  region.upper.in(uppers);
	}

	private BooleanExpression containsLower(Map<String, String> regionMap) {
		return regionMap.keySet().stream()
			.map(upper -> {
				List<String> lowers = Arrays.asList(regionMap.get(upper).split(","));
				return region.upper.eq(upper).and(region.lower.in(lowers));
			})
			.reduce(BooleanExpression::or)
			.orElse(null);
	}
}
