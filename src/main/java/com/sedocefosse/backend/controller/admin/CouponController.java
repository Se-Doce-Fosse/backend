package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("admin/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        List<CouponDTO> coupons = couponService.findAll();
        return ResponseEntity.ok(coupons);
    }
    
    @PostMapping
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody Coupon coupon) {
        CouponDTO createdCoupon = couponService.create(coupon);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }
}
