package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.service.products.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/coupons")
public class CouponPublicController {

    private final CouponService couponService;

    @GetMapping("/validate")
    public ResponseEntity<CouponDTO> validateCoupon(@RequestParam("code") String code) {
        CouponDTO couponDTO = couponService.findActiveCouponByCode(code);
        return ResponseEntity.ok(couponDTO);
    }
}
