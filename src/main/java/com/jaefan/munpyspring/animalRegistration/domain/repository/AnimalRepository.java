package com.jaefan.munpyspring.animalRegistration.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.animalRegistration.domain.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
