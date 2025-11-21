package com.sedocefosse.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.dto.SupplyLogDTO;
import com.sedocefosse.backend.model.SupplyLog;
import com.sedocefosse.backend.repository.SupplyLogRepository;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SupplyLogServiceImpl implements SupplyLogService{
    private final SupplyLogRepository supplyLogRepository;
    
    @Override
    public List<SupplyLogDTO> getSupplyLogBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<SupplyLog> listSupplyLog = supplyLogRepository.findByOrderDateBetweenInclusive(startDate, endDate);
        return listSupplyLog.stream()
        .map(c -> new SupplyLogDTO(c.getId(), c.getInsumoId(), c.getNomeInsumo(), c.getQuantidade(), c.getPreco_compra(), c.getStatus(), c.getOrderDate()))
        .collect(Collectors.toList());
    }

    @Override
    public void createLog(Long insumoId, String nomeInsumo, int quantidade, BigDecimal precoCompra, String status) {
        SupplyLog log = SupplyLog.builder()
                .insumoId(insumoId)
                .nomeInsumo(nomeInsumo)
                .quantidade(quantidade)
                .preco_compra(precoCompra)
                .status(status) // "entrada" ou "saída"
                .orderDate(LocalDateTime.now())
                .build();
        
        supplyLogRepository.save(log);
    }

}
