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
import java.util.stream.Collectors;

import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.model.Supply;

@Service 
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;

    @Autowired
    public SupplyServiceImpl(SupplyRepository supplyRepository){
        this.supplyRepository = supplyRepository;
    }

    @Override
    public Supply create(Supply supply) {
        return supplyRepository.save(supply);
    }


    public Optional<Supply> findSupplyById(Long id){

    }
    
@Override
public List<SupplyResponseDTO> getAllSupplies() {
    List<Supply> supplies = supplyRepository.findAll();
    return supplies.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
}

public void toResponseDTO(Supply supply) {
    SupplyResponseDTO dto = new SupplyResponseDTO();
    dto.id = supply.getId();
    dto.nome = supply.getNome();
    dto.quantidade = supply.getQuantidade();
    dto.unidade_medida = supply.getUnidadeMedida();
    dto.ponto_reposicao = supply.getPontoReposicao();
    return dto;
}
     
    public void deleteSupplyById(Long id){

    }
}