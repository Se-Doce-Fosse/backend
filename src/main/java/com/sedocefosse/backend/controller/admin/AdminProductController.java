package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.aws.S3Service;
import com.sedocefosse.backend.service.products.ProductService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private S3Service s3Service;

    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String sku) {
        productService.deleteProductById(sku);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDTO createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> createProductWithImage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "imageSrc", required = false) MultipartFile imageFile) {
        
        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .isActive(isActive);
        
        if (categoryId != null && !categoryId.isEmpty()) {
            builder.category(new CategoryDTO(categoryId, null, null));
        }
        
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadImage(imageFile, "produtos");
                builder.imageSrc(imageUrl);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        
        ProductDTO product = builder.build();
        ProductDTO createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
  
    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable String id) {
        Product updatedProduct = productService.toggleStatus(id);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }
    
    @PutMapping(value = "/{sku}", consumes = "application/json")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String sku, @RequestBody ProductDTO productDetails) {
        ProductDTO updatedProduct = productService.updateProduct(sku, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping(value = "/{sku}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> updateProductWithImage(
            @PathVariable String sku,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "imageSrc", required = false) MultipartFile imageFile) {
        
        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder();
        if (name != null) builder.name(name);
        if (description != null) builder.description(description);
        if (price != null) builder.price(price);
        if (quantity != null) builder.quantity(quantity);
        if (isActive != null) builder.isActive(isActive);
        
        if (categoryId != null && !categoryId.isEmpty()) {
            builder.category(new CategoryDTO(categoryId, null, null));
        }
        
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Deletar imagem antiga
                Optional<ProductDTO> currentProduct = productService.findProductBySku(sku);
                if (currentProduct.isPresent() && currentProduct.get().getImageSrc() != null 
                        && currentProduct.get().getImageSrc().contains("amazonaws.com")) {
                    try {
                        s3Service.deleteImage(currentProduct.get().getImageSrc());
                    } catch (Exception e) {
                        System.err.println("Erro ao deletar imagem antiga: " + e.getMessage());
                    }
                }
                
                String imageUrl = s3Service.uploadImage(imageFile, "produtos/" + sku);
                builder.imageSrc(imageUrl);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        
        ProductDTO productDetails = builder.build();
        ProductDTO updatedProduct = productService.updateProduct(sku, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable String sku) {
        Optional<ProductDTO> productDto = productService.findProductBySku(sku);

        return productDto.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
}