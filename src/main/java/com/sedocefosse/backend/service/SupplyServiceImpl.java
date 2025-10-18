package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.model.Unit;
import com.sedocefosse.backend.repository.SupplyRepository;
import com.sedocefosse.backend.repository.UnitRepository;
import com.sedocefosse.backend.service.SupplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.SupplyDTO;
import com.sedocefosse.backend.dto.SupplyResponseDTO;

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
    public SupplyDTO create(SupplyDTO supply) {

        Supply newSupply = new Supply();

        newSupply.setNome(supply.getName());
        System.out.println("QUANTIDADE REQUEST: "+supply.getQuantity());
        newSupply.setQuantidade(supply.getQuantity());
        System.out.println("QUANTIDADE SALVA: "+ newSupply.getQuantidade());
        newSupply.setPreco_compra(supply.getPurchasePrice());
        newSupply.setPonto_reposicao(supply.getReorderPoint());
        newSupply.setEmbalagem(supply.getIsPackaging());

        Unit newUnit = unitRepository.findById(supply.getUnityId())
            .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com o id: " + supply.getUnityId()));
        newSupply.setUnidade(newUnit);

        return toSupplyDTO(supplyRepository.save(newSupply));
    }


    @Override
    public Optional<Supply> findSupplyById(Long id) {
        return supplyRepository.findById(id);
    }
    
    @Override
    public List<SupplyDTO> getAllSupplies() {
        List<SupplyDTO> supplies = supplyRepository.findAll().stream()
            .map(this::toSupplyDTO)
            .collect(Collectors.toList());
        return supplies;
    }

    public SupplyDTO toSupplyDTO(Supply supply) {
        SupplyDTO dto = new SupplyDTO();
        dto.setId(supply.getId());
        dto.setName(supply.getNome());
        dto.setUnityId(supply.getUnidade().getId());
        dto.setUnityName(supply.getUnidade().getNome());
        dto.setQuantity(supply.getQuantidade());
        dto.setPurchasePrice(supply.getPreco_compra());
        dto.setReorderPoint(supply.getPonto_reposicao());
        dto.setIsPackaging(supply.getEmbalagem());
        return dto;
    }

    @Override
    @Transactional 
    public SupplyDTO update(Long id, SupplyDTO updateDTO) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo não encontrado com o id: " + id));

        existingSupply.setNome(updateDTO.getName());
        existingSupply.setQuantidade(updateDTO.getQuantity());
        existingSupply.setPreco_compra(updateDTO.getPurchasePrice());
        existingSupply.setPonto_reposicao(updateDTO.getReorderPoint());
        existingSupply.setEmbalagem(updateDTO.getIsPackaging());

        Unit newUnit = unitRepository.findById(updateDTO.getUnityId())
            .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com o id: " + id));
        existingSupply.setUnidade(newUnit);
        
        Supply savedSupply = supplyRepository.save(existingSupply);

        return toSupplyDTO(savedSupply); 
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