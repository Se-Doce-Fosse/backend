package com.sedocefosse.backend.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produto_ingrediente")
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@IdClass(ProductSupplyPK.class)
public class ProductSupply {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_sku", referencedColumnName = "sku")
    private Product product;
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ingrediente_id", referencedColumnName = "id")
    private Supply supply;

    @Column(name = "quantidade_utilizada")
    private double quantidade;

}
