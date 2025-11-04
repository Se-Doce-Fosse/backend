package com.sedocefosse.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sedocefosse.backend.dto.SupplyLogDTO;
import com.sedocefosse.backend.model.SupplyLog;
import com.sedocefosse.backend.repository.SupplyLogRepository;

import lombok.*;

@AllArgsConstructor
public class SupplyLogServiceImpl implements SupplyLogService{
    private final SupplyLogRepository supplyLogRepository;
    
    @Override
    public List<SupplyLogDTO> getSupplyLogBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<SupplyLog> listSupplyLog = supplyLogRepository.findByOrderDateBetween(startDate, endDate);
        return listSupplyLog.stream()
        .map(c -> new SupplyLogDTO(c.getId(), c.getInsumoId(), c.getNomeInsumo(), c.getQuantidade(), c.getPreco_compra(), c.getStatus(), c.getOrderDate()))
        .collect(Collectors.toList());
    }

}
