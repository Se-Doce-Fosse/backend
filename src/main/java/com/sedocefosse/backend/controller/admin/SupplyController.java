package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.SupplyDTO;
import com.sedocefosse.backend.service.SupplyService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/supplies")
public class SupplyController {

    private final SupplyService supplyService;

    @Autowired
    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping
    public ResponseEntity<List<SupplyDTO>> getAllSupplies() {
        List<SupplyDTO> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(supplies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyDTO> update(@PathVariable Long id, @RequestBody SupplyDTO updateDTO) {
        SupplyDTO updatedSupply = supplyService.update(id, updateDTO);
        return ResponseEntity.ok(updatedSupply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupplyById(id);
        return ResponseEntity.ok("Supply deletado com sucesso");
    }

    @PostMapping
    public ResponseEntity<SupplyDTO> createSupply (@RequestBody SupplyDTO supply){
        SupplyDTO supplyCreated = supplyService.create(supply);
        return new ResponseEntity<>(supplyCreated, HttpStatus.CREATED);
    }

}