package com.visualsearch.finder.Interface;


import com.visualsearch.finder.products.ProductsTF;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by poojab26 on 07-Apr-18.
 */


public interface RetrofitInterface {
    @GET("raw/c2854a0b65ddf79194b89bdb51a77140d84c3e0f/product.json")
    Call<ProductsTF> getProductList();

}
