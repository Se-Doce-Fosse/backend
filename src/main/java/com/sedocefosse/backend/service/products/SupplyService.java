package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.model.Supply;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;

@Service
public interface SupplyService {
    SupplyResponseDTO create(Supply supply);

    Optional<Supply> findSupplyById(Long id);
    
    List<SupplyResponseDTO> getAllSupplies();
     
    void deleteSupplyById(Long id);

    SupplyResponseDTO update(Long id, SupplyUpdateDTO updateDTO);

}
