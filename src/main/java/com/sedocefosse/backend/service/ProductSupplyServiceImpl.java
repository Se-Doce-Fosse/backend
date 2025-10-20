package com.sedocefosse.backend.service;

import com.sedocefosse.backend.configs.exceptions.InsufficientSupplyException;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.ProductSupply;
import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.repository.ProductSupplyRepository;
import com.sedocefosse.backend.repository.SupplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSupplyServiceImpl implements ProductSupplyService {

    private final ProductSupplyRepository productSupplyRepository;
    private final SupplyRepository supplyRepository;

    public ProductSupplyServiceImpl(SupplyRepository supplyRepository, ProductSupplyRepository productSupplyRepository) {
        this.supplyRepository = supplyRepository;
        this.productSupplyRepository = productSupplyRepository;
    }

    @Override
    public void updateSupplyInventory(String productSku, Integer quantity) {
        List<ProductSupply> productSupplyList = productSupplyRepository.findByProductSku(productSku);

        for (ProductSupply productSupply : productSupplyList) {
            Supply supply = productSupply.getSupply();

            if (supply != null) {
                double decreaseQuantity = quantity * productSupply.getQuantidade();
                double newQuantity = supply.getQuantidade() - decreaseQuantity;

                if (newQuantity < 0) {
                    throw new InsufficientSupplyException(String.format("Not enough %s", supply.getNome()));
                }
                supply.setQuantidade(newQuantity);

                supplyRepository.save(supply);
            } else {
                throw new InsufficientSupplyException("Product supply not found");
            }
        }
    }
}
