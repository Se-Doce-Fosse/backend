package com.sedocefosse.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatusEnum {

    PREPARANDO("Seu pedido está sendo preparado."),
    ACEITO("Seu produto foi aceito e será preparado em breve."),
    CANCELADO("Desculpe, seu pedido foi cancelado."),
    ENTREGUE("OBA! Seu pedido chegou."),
    ROTA("Seu pedido está em rota de entrega");

    private String message;

}
