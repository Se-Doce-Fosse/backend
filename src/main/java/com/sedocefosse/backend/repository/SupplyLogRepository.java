package com.sedocefosse.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.SupplyLog;

@Repository 
public interface SupplyLogRepository extends JpaRepository<SupplyLog, Long> {

}
