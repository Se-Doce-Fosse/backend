package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.SupplyResponseDTO;
import com.sedocefosse.backend.dto.SupplyUpdateDTO;
import com.sedocefosse.backend.model.Supply;
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
    public ResponseEntity<List<SupplyResponseDTO>> getAllSupplies() {
        List<SupplyResponseDTO> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(supplies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponseDTO> update(@PathVariable Long id, @RequestBody SupplyUpdateDTO updateDTO) {
        SupplyResponseDTO updatedSupply = supplyService.update(id, updateDTO);
        return ResponseEntity.ok(updatedSupply);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupply(@PathVariable Long id) { supplyService.deleteSupplyById(id);}

    @PostMapping
    public ResponseEntity<SupplyResponseDTO> createSupply (@RequestBody Supply supply){
        SupplyResponseDTO supplyCreated = supplyService.create(supply);
        return new ResponseEntity<>(supplyCreated, HttpStatus.CREATED);
    }

}