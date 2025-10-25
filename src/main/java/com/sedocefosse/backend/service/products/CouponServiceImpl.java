package com.sedocefosse.backend.service.products;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.repository.products.CouponRepository;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    
    private final CouponRepository couponRepository;
    
    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    
    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }
}
