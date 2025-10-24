package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;
import com.sedocefosse.backend.model.Coupon;
import java.util.List;

public interface CouponService {
    
    List<Coupon> findAll();

    void deleteCuponById(Long cuponId);

    CouponDTO update(Long id, CouponDTO updateDTO);

}
