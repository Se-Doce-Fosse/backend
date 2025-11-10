package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.CouponDTO;
import com.sedocefosse.backend.model.Coupon;
import com.sedocefosse.backend.repository.products.CouponRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

    @Override
    public void deleteCuponById(Long cuponId) {
        if (!couponRepository.existsById(cuponId)) {
            throw new ResourceNotFoundException("Cupom não encontrado com o ID: "+cuponId);
        }

        try {
            couponRepository.deleteById(cuponId);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir o cupom pois já foi utilizado em pedidos.",
                    exception
            );
        }
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

    @Override
    public CouponDTO findActiveCouponByCode(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código do cupom é obrigatório.");
        }

        Coupon coupon = couponRepository
                .findByCodigoIgnoreCase(codigo.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupom não encontrado."));

        if (!Boolean.TRUE.equals(coupon.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cupom inativo.");
        }

        if (coupon.getValidade() != null && coupon.getValidade().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cupom expirado.");
        }

        return toDTO(coupon);
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
