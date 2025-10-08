package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.repository.SupplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;

@Service
public interface SupplyService {
    SupplyResponseDTO create(Supply supply);

    Optional<Supply> findSupplyById(Long id);
    
    List<SupplyResponseDTO> getAllSupplies();
     
    void deleteSupplyById(Long id);

    SupplyResponseDTO update(Long id, SupplyUpdateDTO updateDTO);

}
