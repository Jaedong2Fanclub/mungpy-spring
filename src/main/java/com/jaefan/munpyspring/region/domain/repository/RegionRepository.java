package com.jaefan.munpyspring.region.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaefan.munpyspring.region.domain.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
