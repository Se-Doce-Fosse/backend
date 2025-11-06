package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.configs.exceptions.InsufficientSupplyException;
import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductSupplyDTO;
import com.sedocefosse.backend.model.ProductSupply;
import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.repository.products.ProductSupplyRepository;
import com.sedocefosse.backend.repository.products.SupplyRepository;
import com.sedocefosse.backend.service.mapper.ProductsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductSupplyServiceImpl implements ProductSupplyService {

    private final ProductSupplyRepository productSupplyRepository;
    private final SupplyRepository supplyRepository;
    private final ProductsMapper productsMapper;

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

    @Override
    public void productSupplyRelation(List<ProductSupplyDTO> supplyDTOList, ProductDTO productDTO) {
        for (ProductSupplyDTO productSupplyDTO : supplyDTOList) {
            switch (productSupplyDTO.getProductSupplyEnum()) {
                case CREATE_ENUM -> {
                    Supply supply = supplyRepository.findById(productSupplyDTO.getSupplyId()).orElseThrow(() -> new ResourceNotFoundException("Supply not found"));

                    ProductSupply productSupply = new ProductSupply();

                    productSupply.setProduct(productsMapper.toEntity(productDTO));
                    productSupply.setSupply(supply);

                    productSupplyRepository.save(productSupply);
                }
                case DELETE_ENUM -> {
                    productSupplyRepository.deleteByProductIdAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId());
                }
                case UPDATE_ENUM -> {
                    ProductSupply productSupply = productSupplyRepository.findByProductIdAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId())
                            .orElseThrow(() -> new ResourceNotFoundException("Relation not found"));

                    productSupply.setQuantidade(productSupplyDTO.getQuantity());
                }
            }
        }

    }
}
