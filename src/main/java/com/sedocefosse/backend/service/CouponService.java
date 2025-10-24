package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import java.util.List;

public interface CouponService {
    
    List<CouponDTO> findAll();
    
    CouponDTO create(Coupon coupon);
}
