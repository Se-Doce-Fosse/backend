package com.sedocefosse.backend.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class ProductSupplyPK implements Serializable {
    private Product product;
    private Supply supply;

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSupplyPK that = (ProductSupplyPK) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(supply, that.supply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, supply);
    }
}
