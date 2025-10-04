package com.sedocefosse.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.Supply;

@Repository 
public interface SupplyRepository extends JpaRepository<Supply, Long> {

}
