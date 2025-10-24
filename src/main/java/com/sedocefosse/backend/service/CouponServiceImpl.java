package com.sedocefosse.backend.service;

import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.CouponDTO;
import org.springframework.stereotype.Service;

import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.repository.CouponRepository;

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

    @Override
    public void deleteCuponById(Long cuponId) {
        if (!couponRepository.existsById(cuponId)) {
            throw new ResourceNotFoundException("Cupom não encontrado com o ID: "+cuponId);
        }

        couponRepository.deleteById(cuponId);
    }

    @Override
    public CouponDTO update(Long id, CouponDTO updateDTO) {
        Coupon couponExisted = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado com o ID: "+id));

        couponExisted.setAtivo(updateDTO.getAtivo());
        couponExisted.setCodigo(updateDTO.getCodigo());
        couponExisted.setValidade(updateDTO.getValidade());
        couponExisted.setUnico(updateDTO.getUnico());
        couponExisted.setValorDesc(updateDTO.getValorDesc());

        Coupon couponUpdated = couponRepository.save(couponExisted);

        return toDTO(couponUpdated);
    }

    public CouponDTO toDTO(Coupon coupon) {
        CouponDTO couponDTO = new CouponDTO();

        couponDTO.setId(coupon.getId());
        couponDTO.setAtivo(coupon.getAtivo());
        couponDTO.setCodigo(coupon.getCodigo());
        couponDTO.setValidade(coupon.getValidade());
        couponDTO.setUnico(coupon.getUnico());
        couponDTO.setValorDesc(coupon.getValorDesc());

        return couponDTO;
    }


}
