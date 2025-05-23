package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.user.domain.model.User;

public interface ShelterRepository extends JpaRepository<Shelter, Long>, ShelterRepositoryCustom {

	Optional<Shelter> findByName(String name);

	Optional<Shelter> findByUser(User user);

	Optional<Shelter> findByNameAndTelNo(String name, String telno);

	Optional<Shelter> findByUserId(Long userId);
}
