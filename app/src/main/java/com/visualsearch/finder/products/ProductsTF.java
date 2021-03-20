package com.visualsearch.finder.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by poojab26 on 09-Apr-18.
 */

public class ProductsTF {

    @SerializedName("products")
    @Expose
    private ArrayList<ProductTF> products = null;

    public ArrayList<ProductTF> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductTF> products) {
        this.products = products;
    }

}