package com.jaefan.munpyspring.animalinfo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.animalinfo.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animalinfo.domain.model.PublicAnimal;

@Repository
public interface ProtectionAnimalRepository extends JpaRepository<ProtectionAnimal, Long> {
}
