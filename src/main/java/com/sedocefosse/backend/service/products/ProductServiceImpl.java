package com.sedocefosse.backend.service.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sedocefosse.backend.configs.exceptions.InsufficientSupplyException;
import com.sedocefosse.backend.configs.exceptions.ResourceInUseException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.RelatedProductDTO;
import com.sedocefosse.backend.model.Category;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.order.OrderRepository;
import com.sedocefosse.backend.repository.products.CategoryRepository;
import com.sedocefosse.backend.repository.products.ProductRepository;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSupplyService productSupplyService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO product) {        
        product.setSku(UUID.randomUUID().toString());

        Product newProduct = new Product();
        newProduct.setSku(product.getSku());
        newProduct.setNome(product.getName());
        newProduct.setDescricao(product.getDescription());
        newProduct.setValor(new BigDecimal(product.getPrice()));
        newProduct.setQuantidade(product.getQuantity());
        newProduct.setImagemUrl(product.getImageSrc());
        newProduct.setAtivo(product.getIsActive());

        Product savedProduct = productRepository.save(newProduct);

        Optional<Category> optionalCategory = categoryRepository.findById(product.getCategory().getId());

        if (optionalCategory.isPresent()) {            
            Category category = optionalCategory.get();
            List<String> products = category.getProdutos();
            products.add(product.getSku());
            category.setProdutos(products);
            categoryRepository.save(category);
        }

        if(!product.getProductSupply().isEmpty()){
            productSupplyService.productSupplyRelation(product.getProductSupply(), product);
        }

        if (product.getQuantity() > 0) {
            productSupplyService.updateSupplyInventory(product.getSku(), product.getQuantity());
        }

        return mapToProductDTO(savedProduct);
    }

    @Override
    public Optional<ProductDTO> findProductBySku(String sku) {
        Optional<Product> product = productRepository.findById(sku);

        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Produto não encontrado com SKU: " + sku);
        }

        ProductDTO dto = this.mapToProductDTO(product.get());
        Category category = categoryRepository.findByProdutos(product.get().getSku());
        dto.setCategory(new CategoryDTO(category.getId(), category.getNome(), null));
        return Optional.of(dto);
    }

    @Override
    @Transactional
    public void deleteProductById(String sku) {
        boolean referenced = orderRepository.existsByProductsContains(sku);
        System.out.println("Produto referenciado em pedidos: " + referenced);
        if (referenced) {
            throw new ResourceInUseException("Não é possível deletar o produto. Existem pedidos referenciando o SKU: " + sku);
        }
        productRepository.deleteById(sku);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            ProductDTO dto = this.mapToProductDTO(product);
            Category category = categoryRepository.findByProdutos(product.getSku());
            if (category != null) {
                dto.setCategory(new CategoryDTO(category.getId(), category.getNome(), null));
            }
            return dto;
        }).collect(Collectors.toList());
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
        List<ProductDetailsDTO> productDetailsDTOs = products.stream()
                .map(this::mapToProductDetailsDTO)
                .collect(Collectors.toList());

        return new CategoryDTO(
                category.getId(),
                category.getNome(),
                productDetailsDTOs
        );
    }

    private ProductDTO mapToProductDTO(Product product) {
        String valorFormatado = "R$ " + product.getValor().toString().replace('.', ',');

        return ProductDTO.builder()
                .sku(product.getSku())
                .name(product.getNome())
                .price(valorFormatado)
                .imageSrc(product.getImagemUrl())
                .description(product.getDescricao())
                .isActive(product.getAtivo())
                .quantity(product.getQuantidade())
                .build();
    }


    @Override
    public Optional<ProductDetailsDTO> findProductDetailsBySku(String sku) {
        return productRepository.findById(sku).map(product -> {

            ProductDetailsDTO productDetails = this.mapToProductDetailsDTO(product);

            Category category = categoryRepository.findByProdutos(sku);
            category.getProdutos().remove(sku);

            if (!category.getProdutos().isEmpty()) {
                List<Product> relatedProducts = productRepository.findAllById(category.getProdutos());

                List<RelatedProductDTO> relatedProductsDTOs = relatedProducts.stream()
                        .map(this::mapToRelatedProductDTO)
                        .collect(Collectors.toList());
                productDetails.setRelatedProducts(relatedProductsDTOs);
                return productDetails;
            }

            return productDetails;
        });
    }

    private ProductDetailsDTO mapToProductDetailsDTO(Product product) {
        String valorFormatado = "R$ " + product.getValor().toString().replace('.', ',');
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setId(product.getSku());
        dto.setName(product.getNome());
        dto.setDescription(product.getDescricao());
        dto.setPrice(valorFormatado);
        dto.setImageSrc(product.getImagemUrl());
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
    @Transactional
    public ProductDTO updateProduct(String sku, ProductDTO productDto) {
        Optional<Product> optionalProduct = productRepository.findById(sku);
        if (optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Produto não encontrado com SKU: " + sku);
        }
        
        Product product = optionalProduct.get();

        // Atualizar os campos do produto
        if (productDto.getName() != null) {
            product.setNome(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescricao(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setValor(new BigDecimal(productDto.getPrice().replace("R$ ", "").replace(",", ".")));
        }
        if (productDto.getImageSrc() != null) {
            product.setImagemUrl(productDto.getImageSrc());
        }
        if (productDto.getIsActive() != null) {
            product.setAtivo(productDto.getIsActive());
        }

        if (!productDto.getProductSupply().isEmpty()) {
            productSupplyService.productSupplyRelation(productDto.getProductSupply(), productDto);
        }

        if (productDto.getQuantity() != null) {
            int newQuantity = productDto.getQuantity();
            int oldQuantity = product.getQuantidade() != null ? product.getQuantidade(): 0;
            if (newQuantity < 0) {
                throw new InsufficientSupplyException("Quantity cannot be negative");
            }

            if (newQuantity != oldQuantity) {
                if (newQuantity > oldQuantity) {
                    productSupplyService.updateSupplyInventory(product.getSku(), newQuantity - oldQuantity);
                }
                product.setQuantidade(newQuantity);
            }

        }

        if (productDto.getCategory() != null && productDto.getCategory().getId() != null) {
            updateCategoryProducts(sku, productDto.getCategory().getId());
        }

        Product updatedProduct = productRepository.save(product);
        
        return mapToProductDTO(updatedProduct);
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

    private void updateCategoryProducts(String sku, String categoryIdRequest) {
        Category currentCategory = categoryRepository.findByProdutos(sku);
        if (currentCategory.getId().equals(categoryIdRequest)) {
            return;
        }

        currentCategory.getProdutos().remove(sku);
        categoryRepository.save(currentCategory);

        Category newCategory = categoryRepository.findById(categoryIdRequest)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + categoryIdRequest));

        List<String> produtos = newCategory.getProdutos();
        produtos.add(sku);
        newCategory.setProdutos(produtos);
        categoryRepository.save(newCategory);
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
