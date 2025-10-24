package com.sedocefosse.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.Coupon;

@Repository 
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
}
