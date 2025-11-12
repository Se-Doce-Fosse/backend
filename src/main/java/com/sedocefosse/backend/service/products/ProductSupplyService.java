package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductSupplyDTO;
import java.util.List;

public interface ProductSupplyService {
    void updateSupplyInventory(String productSku, Integer quantity);

    void productSupplyRelation(List<ProductSupplyDTO> supplyDTOList, ProductDTO productDTO);

}
