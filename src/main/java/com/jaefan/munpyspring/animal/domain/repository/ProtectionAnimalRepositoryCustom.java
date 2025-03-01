package com.jaefan.munpyspring.animal.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;

public interface ProtectionAnimalRepositoryCustom {

	List<ProtectionAnimal> findProtectionAnimals(AnimalSearchCondition searchCondition);

	List<ProtectionAnimal> findProtectionAnimalsWithPagination(AnimalSearchCondition searchCondition, Pageable pageable);
}
