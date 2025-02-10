package com.jaefan.munpyspring.recommendation.domain.repository;

import com.jaefan.munpyspring.recommendation.domain.model.RequestCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestCountRepository extends JpaRepository<RequestCount, Long> {
	//@Query("SELECT r FROM RequestCount r WHERE r.id = :id FOR UPDATE")
	@Query("SELECT r FROM RequestCount r WHERE r.id = :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	RequestCount findByIdForUpdate(@Param("id") Long id);
}
