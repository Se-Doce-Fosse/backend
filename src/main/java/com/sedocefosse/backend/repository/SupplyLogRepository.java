package com.sedocefosse.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.SupplyLog;

@Repository 
public interface SupplyLogRepository extends JpaRepository<SupplyLog, Long> {
    List<SupplyLog> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT s FROM SupplyLog s WHERE s.orderDate >= :startDate AND s.orderDate <= :endDate")
    List<SupplyLog> findByOrderDateBetweenInclusive(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
