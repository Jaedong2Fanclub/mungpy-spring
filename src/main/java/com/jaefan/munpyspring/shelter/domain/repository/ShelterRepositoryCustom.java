package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.List;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

public interface ShelterRepositoryCustom {
	List<Shelter> findByRegion(String upper, String lower);
}
