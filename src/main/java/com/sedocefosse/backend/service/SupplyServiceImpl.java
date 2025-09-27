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
public List<Supply> getAllSupplies() {
    List<Supply> supplies = supplyRepository.findAll();
    return supplies;
}

public SupplyResponseDTO toResponseDTO(Supply supply) {
    SupplyResponseDTO dto = new SupplyResponseDTO();
    dto.setId(supply.getId());
    dto.setNome(supply.getNome());
    dto.setUnidadeId(supply.getUnidade().getId());
    dto.setUnidadeNome(supply.getUnidade().getNome()); // if Unit has a name field
    dto.setQuantidade(supply.getQuantidade());
    dto.setPrecoCompra(supply.getPreco_compra());
    dto.setPontoReposicao(supply.getPonto_reposicao());
    return dto;
}
    
public void deleteSupplyById(Long id){

    }
}