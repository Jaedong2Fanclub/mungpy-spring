package com.jaefan.munpyspring.animal.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;

@Repository
public interface ProtectionAnimalRepository extends JpaRepository<ProtectionAnimal, Long> {

	Optional<ProtectionAnimal> findByNoticeNo(String name);
}
