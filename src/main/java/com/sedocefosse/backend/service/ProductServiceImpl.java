package com.sedocefosse.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sedocefosse.backend.service.ProductService;
import org.springframework.stereotype.Service;

import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.RelatedProductDTO;
import com.sedocefosse.backend.model.Category;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.CategoryRepository;
import com.sedocefosse.backend.repository.ProductRepository;

@Service 
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDetailsDTO create(Product product) {        
        product.setSku(UUID.randomUUID().toString());
        Product savedProduct = productRepository.save(product);
        return mapToProductDetailsDTO(savedProduct);
    }

    @Override
    public Optional<Product> findProductById(String sku) {
        return productRepository.findById(sku);
    }

    @Override
    public void deleteProductById(String sku) {
        productRepository.deleteById(sku);
    }

    @Override
    public List<CategoryDTO> getAllProductsGroupedByCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            List<String> productSkus = category.getProdutos();

            List<Product> products = productRepository.findBySkuIn(productSkus);

            return mapToCategoryDTO(category, products);

        }).collect(Collectors.toList());
    }

    private CategoryDTO mapToCategoryDTO(Category category, List<Product> products) {
        List<ProductDTO> productDTOs = products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());

        return new CategoryDTO(
                category.getId(),
                category.getNome(),
                productDTOs
        );
    }

    private ProductDTO mapToProductDTO(Product product) {
        String valorFormatado = "R$ " + product.getValor().toString().replace('.', ',');

        return new ProductDTO(
                product.getSku(),
                product.getNome(),
                valorFormatado,                
                product.getImagemUrl(),
                product.getNome(),
                product.getDescricao(),
                product.getAtivo(),
                null, 
                null, 
                null, 
                null
        );
    }

    @Override
    public Optional<ProductDTO> findProductDetailsBySku(String sku) {
        return productRepository.findById(sku).map(product -> {

            ProductDTO productDTO = this.mapToProductDTO(product);

            Category category = categoryRepository.findByProdutos(sku);
            category.getProdutos().remove(sku);
            System.out.println("Categoria encontrada:");
            System.out.println(category.getNome());

            productDTO.setCategoryName(category.getNome());
            productDTO.setCategoryId(category.getId());

            if (!category.getProdutos().isEmpty()) {
                List<Product> relatedProducts = productRepository.findAllById(category.getProdutos());

                List<RelatedProductDTO> relatedProductsDTOs = relatedProducts.stream()
                        .map(this::mapToRelatedProductDTO)
                        .collect(Collectors.toList());
                productDTO.setRelatedProducts(relatedProductsDTOs);
                return productDTO;
            }

            return productDTO;
        });
    }


    private ProductDetailsDTO mapToProductDetailsDTO(Product product) {
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setSku(product.getSku());
        dto.setNome(product.getNome());
        dto.setDescricao(product.getDescricao());
        dto.setValor(product.getValor());
        dto.setImagemUrl(product.getImagemUrl());
        dto.setAtivo(product.getAtivo());
        return dto;
    }

    @Override
    public Product toggleStatus(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return null;
        }
        Product product = optionalProduct.get();
        product.setAtivo(!Boolean.TRUE.equals(product.getAtivo()));
        return productRepository.save(product);
    }
    
    @Override
    public Product updateProduct(String sku, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(sku);
        if (optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Produto não encontrado com SKU: " + sku);
        }
        
        Product product = optionalProduct.get();
        
        // Atualizar os campos do produto
        if (productDetails.getNome() != null) {
            product.setNome(productDetails.getNome());
        }
        if (productDetails.getDescricao() != null) {
            product.setDescricao(productDetails.getDescricao());
        }
        if (productDetails.getValor() != null) {
            product.setValor(productDetails.getValor());
        }
        if (productDetails.getImagemUrl() != null) {
            product.setImagemUrl(productDetails.getImagemUrl());
        }
        if (productDetails.getAtivo() != null) {
            product.setAtivo(productDetails.getAtivo());
        }
        
        return productRepository.save(product);
    }

    private RelatedProductDTO mapToRelatedProductDTO(Product product) {
        return new RelatedProductDTO(
                product.getSku(),
                product.getNome(),
                product.getValor().toString(),
                product.getImagemUrl(),
                null
        );
    }
}

//Teste manual a ser feito no Postman
//POST http://localhost:8080/api/products
//Body (JSON):
// {
//     "name": "Produto Exemplo",
//     "description": "Descrição do Produto Exemplo",
//     "price": 19.99,
//     "imageUrl": "http://exemplo.com/imagem.jpg",
//     "restricaoAlimentar": "Sem glúten"
