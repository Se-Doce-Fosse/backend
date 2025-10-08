package com.sedocefosse.backend.service.impl;

import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.model.Unit;
import com.sedocefosse.backend.repository.SupplyRepository;
import com.sedocefosse.backend.repository.UnitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;

@Service 
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public SupplyServiceImpl(SupplyRepository supplyRepository, UnitRepository unitRepository){
        this.supplyRepository = supplyRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public SupplyResponseDTO create(Supply supply) {
        supplyRepository.save(supply);
        return toResponseDTO(supply);
    }


    @Override
    public Optional<Supply> findSupplyById(Long id) {
        return supplyRepository.findById(id);
    }
    
    @Override
    public List<SupplyResponseDTO> getAllSupplies() {
        List<SupplyResponseDTO> supplies = supplyRepository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
        return supplies;
    }

    public SupplyResponseDTO toResponseDTO(Supply supply) {
        SupplyResponseDTO dto = new SupplyResponseDTO();
        dto.setId(supply.getId());
        dto.setNome(supply.getNome());
        dto.setUnidadeId(supply.getUnidade().getId());
        dto.setUnidadeNome(supply.getUnidade().getNome());
        dto.setQuantidade(supply.getQuantidade());
        dto.setPrecoCompra(supply.getPreco_compra());
        dto.setPontoReposicao(supply.getPonto_reposicao());
        return dto;
    }

    @Override
    @Transactional 
    public SupplyResponseDTO update(Long id, SupplyUpdateDTO updateDTO) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo não encontrado com o id: " + id));

        existingSupply.setNome(updateDTO.getName());
        existingSupply.setQuantidade(updateDTO.getQuantidade());
        existingSupply.setPreco_compra(updateDTO.getPrecoCompra());
        existingSupply.setPonto_reposicao(updateDTO.getPontoReposicao());

        Unit newUnit = unitRepository.findById(updateDTO.getUnidadeId())
            .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com o id: " + id));
        existingSupply.setUnidade(newUnit);
        
        Supply savedSupply = supplyRepository.save(existingSupply);

        return toResponseDTO(savedSupply); 
    }

    @Override
    @Transactional
    public void deleteSupplyById(Long id) {
        if (!supplyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Insumo não encontrado com o id: " + id);
        }
        supplyRepository.deleteById(id);
    }
    
}