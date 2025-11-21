package com.sedocefosse.backend.repository.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    
}
