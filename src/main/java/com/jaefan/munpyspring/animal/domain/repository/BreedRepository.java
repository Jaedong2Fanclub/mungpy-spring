package com.jaefan.munpyspring.animal.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaefan.munpyspring.animal.domain.model.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {

	@Query("SELECT b FROM Breed b WHERE :family IS NULL OR b.family = :family")
	List<Breed> findByFamily(String family);
}
