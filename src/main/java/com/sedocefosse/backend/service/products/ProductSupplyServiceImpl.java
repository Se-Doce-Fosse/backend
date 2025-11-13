package com.sedocefosse.backend.service.products;

import com.sedocefosse.backend.configs.exceptions.InsufficientSupplyException;
import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.ProductSupplyDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.ProductSupply;
import com.sedocefosse.backend.model.Supply;
import com.sedocefosse.backend.repository.products.ProductSupplyRepository;
import com.sedocefosse.backend.repository.products.SupplyRepository;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductSupplyServiceImpl implements ProductSupplyService {

    private final ProductSupplyRepository productSupplyRepository;
    private final SupplyRepository supplyRepository;

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
            if (productSupplyDTO.getProductSupplyEnum() == null) {
                throw new InsufficientSupplyException("Product supply enum not found");
            }
            switch (productSupplyDTO.getProductSupplyEnum()) {
                case CREATE_ENUM -> {
                    productSupplyRepository.findByProductSkuAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId())
                            .ifPresent(existingSupply -> {
                                throw new InsufficientSupplyException("Relacao ja existente");
                            });
                    Supply supply = supplyRepository.findById(productSupplyDTO.getSupplyId()).orElseThrow(() -> new ResourceNotFoundException("Supply not found"));
                    ProductSupply productSupply = new ProductSupply();

                    productSupply.setProduct(toEntity(productDTO));
                    productSupply.setSupply(supply);
                    productSupply.setQuantidade(productSupplyDTO.getQuantity());

                    productSupplyRepository.save(productSupply);
                }
                case DELETE_ENUM -> {
                    productSupplyRepository.findByProductSkuAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId())
                            .ifPresentOrElse(
                                    productSupply -> {
                                        productSupplyRepository.deleteByProductSkuAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId());
                                    },
                                    () -> {
                                        throw new ResourceNotFoundException("Supply not found");
                                    }
                            );
                }
                case UPDATE_ENUM -> {
                    ProductSupply productSupply = productSupplyRepository.findByProductSkuAndSupplyId(productDTO.getSku(), productSupplyDTO.getSupplyId())
                            .orElseThrow(() -> new ResourceNotFoundException("Relation not found"));

                    if (productSupplyDTO.getQuantity() != 0) {
                        productSupply.setQuantidade(productSupplyDTO.getQuantity());
                        productSupplyRepository.save(productSupply);
                    }
                    else {
                        throw new InsufficientSupplyException("Quantity not set");
                    }
                }
            }
        }
    }

    public Product toEntity(ProductDTO productDTO) {
        BigDecimal price = productDTO.getPrice() != null ? (productDTO.getPrice()): new BigDecimal(0);
        Product product = new Product();
        product.setSku(productDTO.getSku());
        product.setNome(productDTO.getName());
        product.setDescricao(productDTO.getDescription());
        product.setValor(price);
        product.setQuantidade(productDTO.getQuantity());
        product.setImagemUrl(productDTO.getImageSrc());
        product.setAtivo(productDTO.getIsActive());

        return product;
    }
}
