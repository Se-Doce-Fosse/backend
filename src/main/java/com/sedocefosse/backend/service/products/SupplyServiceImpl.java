package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.model.Unit;
import com.sedocefosse.backend.repository.products.SupplyRepository;
import com.sedocefosse.backend.repository.products.UnitRepository;
import com.sedocefosse.backend.service.SupplyLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.SupplyDTO;

@Service 
public class SupplyServiceImpl implements SupplyService {
    private final SupplyRepository supplyRepository;
    private final UnitRepository unitRepository;
    private final SupplyLogService supplyLogService;

    @Autowired
    public SupplyServiceImpl(SupplyRepository supplyRepository, UnitRepository unitRepository, SupplyLogService supplyLogService){
        this.supplyRepository = supplyRepository;
        this.unitRepository = unitRepository;
        this.supplyLogService = supplyLogService;
    }

    @Override
    public SupplyDTO create(SupplyDTO supply) {

        Supply newSupply = new Supply();

         if (supply.getUnityId() != null) {
            Unit unit = unitRepository.findById(supply.getUnityId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o id: " + supply.getUnityId()));
            newSupply.setUnidade(unit);
        } else {
            throw new ResourceNotFoundException("Unidade é obrigatória para criar um supply");
        }

        newSupply.setNome(supply.getName());
        newSupply.setQuantidade(supply.getQuantity());
        newSupply.setPreco_compra(supply.getPurchasePrice());
        newSupply.setPonto_reposicao(supply.getReorderPoint());
        newSupply.setEmbalagem(supply.getIsPackaging());

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

        // Guarda a quantidade antiga para calcular a diferença
        double quantidadeAntiga = existingSupply.getQuantidade();
        double tolerancia = 0.0001; // Tolerância para comparação de double

        existingSupply.setNome(updateDTO.getName());
        existingSupply.setQuantidade(updateDTO.getQuantity());
        existingSupply.setPreco_compra(updateDTO.getPurchasePrice());
        existingSupply.setPonto_reposicao(updateDTO.getReorderPoint());
        existingSupply.setEmbalagem(updateDTO.getIsPackaging());

        Unit newUnit = unitRepository.findById(updateDTO.getUnityId())
            .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrado com o id: " + id));
        existingSupply.setUnidade(newUnit);
        
        Supply savedSupply = supplyRepository.save(existingSupply);

        // Calcula a diferença
        double diferencaQuantidade = savedSupply.getQuantidade() - quantidadeAntiga;
        boolean quantidadeMudou = Math.abs(diferencaQuantidade) > tolerancia;
        
        // Registra apenas se tiver mudança na quantidade
        if (quantidadeMudou) {
            String status = diferencaQuantidade > 0 ? "entrada" : "saída";
            int quantidadeAbsoluta = (int) Math.abs(diferencaQuantidade);
            
            supplyLogService.createLog(
                savedSupply.getId(),
                savedSupply.getNome(),
                quantidadeAbsoluta,
                savedSupply.getPreco_compra(),
                status
            );
        }

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