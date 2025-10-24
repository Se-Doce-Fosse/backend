package com.sedocefosse.backend.service;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.repository.CouponRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {
    
    private final CouponRepository couponRepository;
    
    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    
    @Override
    public List<CouponDTO> findAll() {
        return couponRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public CouponDTO create(Coupon coupon) {
        Coupon saved = couponRepository.save(coupon);
        return toDTO(saved);
    }

    private CouponDTO toDTO(Coupon coupon) {
        CouponDTO dto = new CouponDTO();
        dto.setId(coupon.getId());
        dto.setCodigo(coupon.getCodigo());
        dto.setValorDesc(coupon.getValorDesc());
        dto.setValidade(coupon.getValidade());
        dto.setAtivo(coupon.getAtivo());
        dto.setUnico(coupon.getUnico());
        return dto;
    }
}
