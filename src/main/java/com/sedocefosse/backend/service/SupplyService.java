package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Supply;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.sedocefosse.backend.dto.SupplyDTO;

@Service
public interface SupplyService {
    SupplyDTO create(SupplyDTO supply);

    Optional<Supply> findSupplyById(Long id);
    
    List<SupplyDTO> getAllSupplies();
     
    void deleteSupplyById(Long id);

    SupplyDTO update(Long id, SupplyDTO updateDTO);

}
