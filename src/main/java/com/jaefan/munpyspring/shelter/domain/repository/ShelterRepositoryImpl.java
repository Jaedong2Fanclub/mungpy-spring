package com.jaefan.munpyspring.shelter.domain.repository;

import static com.jaefan.munpyspring.region.domain.model.QRegion.*;
import static com.jaefan.munpyspring.shelter.domain.model.QShelter.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShelterRepositoryImpl implements ShelterRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Shelter> findByRegion(String upper, String lower) {
		return queryFactory
			.selectFrom(shelter)
			.join(shelter.region, region)
			.where(upperEq(upper), lowerEq(lower))
			.fetch();
	}

	private BooleanExpression upperEq(String upper) {
		return upper != null ? region.upper.eq(upper) : null;
	}

	private BooleanExpression lowerEq(String lower) {
		return lower != null ? region.lower.eq(lower) : null;
	}
}
