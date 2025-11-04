package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import java.util.List;

public interface CouponService {
    
    List<Coupon> findAll();

    void deleteCuponById(Long cuponId);

    CouponDTO update(Long id, CouponDTO updateDTO);

}
