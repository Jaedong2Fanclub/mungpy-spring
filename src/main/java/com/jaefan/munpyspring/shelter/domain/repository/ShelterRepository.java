package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

	Optional<Shelter> findByNameAndTelno(String name, String telno);
}
