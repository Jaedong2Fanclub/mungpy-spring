package com.jaefan.munpyspring.animal.domain.repository;

import java.util.List;

import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;

public interface ProtectionAnimalRepositoryCustom {

	List<ProtectionAnimal> findProtectionAnimals(AnimalSearchCondition searchCondition);
}
