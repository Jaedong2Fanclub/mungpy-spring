package com.jaefan.munpyspring.animal.domain.repository;

import static com.jaefan.munpyspring.animal.domain.model.QBreed.*;
import static com.jaefan.munpyspring.animal.domain.model.QProtectionAnimal.*;
import static com.jaefan.munpyspring.shelter.domain.model.QShelter.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.animal.domain.model.AnimalGender;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProtectionAnimalRepositoryImpl implements ProtectionAnimalRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProtectionAnimal> findProtectionAnimals(AnimalSearchCondition searchCondition) {
		return queryFactory
			.selectFrom(protectionAnimal)
			.join(protectionAnimal.shelter, shelter).fetchJoin()
			.join(protectionAnimal.breed, breed).fetchJoin()
			.where(
				shelter.id.in(searchCondition.getShelterIds()),
				animalTypeEq(searchCondition.getAnimalType()),
				breedTypeEq(searchCondition.getBreedType()),
				genderEq(searchCondition.getGender())
			)
			.fetch();
	}

	private BooleanExpression animalTypeEq(AnimalType animalType) {
		return animalType != null ? protectionAnimal.type.eq(animalType) : null;
	}

	private BooleanExpression breedTypeEq(String breedType) {
		return breedType != null ? breed.breedName.eq(breedType) : null;
	}

	private BooleanExpression genderEq(AnimalGender gender) {
		return gender != null ? protectionAnimal.gender.eq(gender) : null;
	}
}
