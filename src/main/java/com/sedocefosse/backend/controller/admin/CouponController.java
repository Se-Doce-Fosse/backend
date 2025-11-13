package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.service.products.CouponService;
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
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        Coupon coupon = toEntity(couponDTO);
        CouponDTO createdCoupon = couponService.create(coupon);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@PathVariable Long id) {
        couponService.deleteCuponById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @RequestBody CouponDTO couponDTO) {
        CouponDTO updatedCouponDTO = couponService.update(id, couponDTO);
        return ResponseEntity.ok(updatedCouponDTO);
    }

    private Coupon toEntity(CouponDTO dto) {
        Coupon coupon = new Coupon();
        coupon.setCodigo(dto.getCodigo());
        coupon.setValorDesc(dto.getValorDesc());
        coupon.setValidade(dto.getValidade());
        coupon.setAtivo(dto.getAtivo());
        coupon.setUnico(dto.getUnico());
        return coupon;
    }
}
