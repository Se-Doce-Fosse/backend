package com.sedocefosse.backend.repository;

import com.sedocefosse.backend.model.Category;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findByProdutos(String sku);
}