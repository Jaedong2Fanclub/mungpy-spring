package com.jaefan.munpyspring.animal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaefan.munpyspring.animal.domain.model.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {
}
