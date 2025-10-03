package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.model.Unit;
import com.sedocefosse.backend.repository.SupplyRepository;
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

    @Autowired
    public SupplyServiceImpl(SupplyRepository supplyRepository){
        this.supplyRepository = supplyRepository;
    }

    @Override
    public Supply create(Supply supply) {
        return supplyRepository.save(supply);
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