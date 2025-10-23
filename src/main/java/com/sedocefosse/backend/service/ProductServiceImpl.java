package com.sedocefosse.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sedocefosse.backend.service.ProductService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sedocefosse.backend.configs.exceptions.ResourceNotFoundException;
import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.RelatedProductDTO;
import com.sedocefosse.backend.model.Category;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.CategoryRepository;
import com.sedocefosse.backend.repository.OrderRepository;
import com.sedocefosse.backend.repository.ProductRepository;

@Service 
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

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
            List<String> produtos = category.getProdutos();
            produtos.add(savedProduct.getSku());
            category.setProdutos(produtos);
            categoryRepository.save(category);
        }
        return mapToProductDTO(savedProduct);
    }

    @Override
    public Optional<ProductDTO> findProductBySku(String sku) {
        Optional<Product> product = productRepository.findById(sku);

        if (!product.isPresent()) {
            throw new ResourceNotFoundException("Produto não encontrado com SKU: " + sku);
        }

        ProductDTO dto = this.mapToProductDTO(product.get());
        Category category = categoryRepository.findByProdutos(product.get().getSku());
        dto.setCategory(new CategoryDTO(category.getId(), category.getNome(), null));
        return Optional.of(dto);
    }

    @Override
    public void deleteProductById(String sku) {
        boolean referenced = orderRepository.existsByProductsContaining(sku);
        if (referenced) {
            throw new IllegalStateException("Não é possível deletar o produto. Existem pedidos referenciando o SKU: " + sku);
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
                product.getDescricao(),
                product.getAtivo(),
                product.getQuantidade(),
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

            CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getNome(), null);
            productDTO.setCategory(categoryDTO);

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
        if (productDto.getQuantity() != null) {
            product.setQuantidade(productDto.getQuantity());
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
