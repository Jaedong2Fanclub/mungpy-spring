package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

public interface ShelterRepositoryCustom {

	List<Shelter> findByRegion(Map<String, String> regionMap);

	List<Shelter> findByRegionWithPagination(Map<String, String> regionMap, Pageable pageable);
}
