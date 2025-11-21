package com.sedocefosse.backend.repository.shoppingCart;

import com.sedocefosse.backend.model.shoppingCart.ShoppingCartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LegacyShoppingCartRepository extends MongoRepository<ShoppingCartEntity, String> {

}
