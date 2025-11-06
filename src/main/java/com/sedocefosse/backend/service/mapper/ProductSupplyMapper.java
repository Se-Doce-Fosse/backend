package com.sedocefosse.backend.service.mapper;

import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductSupplyDTO;
import com.sedocefosse.backend.dto.SupplyDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.ProductSupply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductSupplyMapper {
    ProductSupplyDTO toDTO(ProductSupply productSupply);
}
