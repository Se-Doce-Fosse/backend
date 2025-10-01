package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.service.SupplyService;
import com.sedocefosse.backend.service.SupplyServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/admin/supplies")
public class SupplyController {

    private final SupplyService supplyService;

    @Autowired
    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }


    @GetMapping
    public ResponseEntity<List<SupplyResponseDTO>> getAllSupplies() {
        List<SupplyResponseDTO> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(supplies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponseDTO> update(@PathVariable Long id, @RequestBody SupplyUpdateDTO updateDTO) {
        SupplyResponseDTO updatedSupply = supplyService.update(id, updateDTO);
        return ResponseEntity.ok(updatedSupply);
    }

}