package com.todo.behtarinhotel.simpleobjects;

import java.util.ArrayList;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class PayPallParams {

    private Product product;

    public PayPallParams(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
