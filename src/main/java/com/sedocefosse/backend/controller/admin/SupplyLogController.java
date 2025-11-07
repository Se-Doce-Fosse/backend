package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.SupplyLogDTO;
import com.sedocefosse.backend.service.SupplyLogService;

import lombok.AllArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/supplylog")
public class SupplyLogController {
    private final SupplyLogService supplyLogService;

    //GET /admin/supplylog?startDate=2025-10-27T00:00:00&endDate=2025-10-30T00:00:00

    @GetMapping()
    public ResponseEntity<List<SupplyLogDTO>> getSupplyLogBetweenDates(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        List<SupplyLogDTO> supplyLog = supplyLogService.getSupplyLogBetweenDates(startDate, endDate);
        return ResponseEntity.ok(supplyLog);
    }
}


