package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import java.util.List;

public interface CouponService {
    
    List<CouponDTO> findAll();

    CouponDTO create(Coupon coupon);

    void deleteCuponById(Long cuponId);

    CouponDTO update(Long id, CouponDTO updateDTO);

}
