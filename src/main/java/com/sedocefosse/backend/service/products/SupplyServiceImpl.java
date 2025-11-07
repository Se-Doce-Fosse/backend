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
import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;

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
    @Transactional
    public SupplyResponseDTO create(Supply supply) {
        // Busca a unidade pelo ID se ela foi enviada
        if (supply.getUnidade() != null && supply.getUnidade().getId() != null) {
            Unit unit = unitRepository.findById(supply.getUnidade().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com o id: " + supply.getUnidade().getId()));
            supply.setUnidade(unit);
        } else {
            throw new ResourceNotFoundException("Unidade é obrigatória para criar um supply");
        }
        
        Supply savedSupply = supplyRepository.save(supply);
        
        // Registra log de entrada quando cria um novo supply
        supplyLogService.createLog(
            savedSupply.getId(),
            savedSupply.getNome(),
            (int) savedSupply.getQuantidade(),
            savedSupply.getPreco_compra(),
            "entrada"
        );
        
        return toResponseDTO(savedSupply);
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

        // Guarda a quantidade antiga para calcular a diferença
        double quantidadeAntiga = existingSupply.getQuantidade();
        double tolerancia = 0.0001; // Tolerância para comparação de double

        existingSupply.setNome(updateDTO.getName());
        existingSupply.setQuantidade(updateDTO.getQuantidade());
        existingSupply.setPreco_compra(updateDTO.getPrecoCompra());
        existingSupply.setPonto_reposicao(updateDTO.getPontoReposicao());

        Unit newUnit = unitRepository.findById(updateDTO.getUnidadeId())
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