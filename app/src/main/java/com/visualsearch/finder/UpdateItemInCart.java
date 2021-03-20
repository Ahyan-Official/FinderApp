package com.visualsearch.finder;


import com.visualsearch.finder.Model.Cart;

public class UpdateItemInCart {
    private Cart cart;

    public UpdateItemInCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
