package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Coupon;
import java.util.List;

public interface CouponService {
    
    List<Coupon> findAll();
    
    Coupon create(Coupon coupon);
}
