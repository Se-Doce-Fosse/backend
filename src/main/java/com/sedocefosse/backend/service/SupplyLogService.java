package com.sedocefosse.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.sedocefosse.backend.dto.SupplyLogDTO;

public interface SupplyLogService {
    List<SupplyLogDTO> getSupplyLogBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    void createLog(Long insumoId, String nomeInsumo, int quantidade, BigDecimal precoCompra, String status);
}
