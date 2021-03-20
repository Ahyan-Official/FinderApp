package com.visualsearch.finder;


import com.visualsearch.finder.Model.Wishlist;

public class UpdateItemInWishlist {
    private Wishlist wishlist;

    public UpdateItemInWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }
}
