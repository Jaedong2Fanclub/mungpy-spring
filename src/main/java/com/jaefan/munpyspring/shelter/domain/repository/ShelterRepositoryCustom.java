package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

public interface ShelterRepositoryCustom {

	List<Shelter> findAll();

	List<Shelter> findAllWithPagination(Pageable pageable);

	List<Shelter> findByRegion(String upper, List<String> lowers);

	List<Shelter> findByRegionWithPagination(String upper, List<String> lowers, Pageable pageable);
}
