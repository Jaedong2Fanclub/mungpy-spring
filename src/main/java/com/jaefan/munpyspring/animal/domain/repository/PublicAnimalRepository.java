package com.jaefan.munpyspring.animal.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.animal.domain.model.PublicAnimal;

@Repository
public interface PublicAnimalRepository extends JpaRepository<PublicAnimal, Long> {

	Optional<PublicAnimal> findByNoticeNo(String name);
}