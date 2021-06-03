package com.visualsearch.finder.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.visualsearch.finder.R;
import com.visualsearch.finder.products.ProductTF;

import java.util.ArrayList;
import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;


public class ProductAdapterTF extends RecyclerView.Adapter<ProductAdapterTF.ViewHolder> {
    public ProductAdapterTF(ArrayList<ProductTF> products, Boolean result) {
        productList = products;
        secondResult = result;
    }

    private final ArrayList<ProductTF> productList;
    private final Boolean secondResult;


    @Override
    public ProductAdapterTF.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(!secondResult){
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_list, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.second_product_item_list, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapterTF.ViewHolder holder, int position) {


            holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView product_name,product_price;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);

        }

        public void bind(final int position) {

            final int min = 0;
            final int max = 4;
            final int random = new Random().nextInt((max - min) + 1) + min;



            final int min1 = 50;
            final int max1 = 300;
            final int random1 = new Random().nextInt((max1 - min1) + 1) + min;

            String listOfFruit[] = {" Beefy T-Shirt", "Nano Premium Cotton T-Shirt", "Regular-Fit Short-Sleeve Slub Henley T-Shirt", "Bitton Short Sleeve Kasum Henley", "Tall Short-Sleeve Beefy T-Shirt"};
            String listOfSuit[] = {" STACY ADAMS Men's Mars Big and Tall Vested 3 Piece Suit", "Van Heusen Boys' 2-Piece Formal Dresswear Suit", "Perry Ellis Men's Two Piece Finished Bottom Slim Fit Suit", "Kenneth Cole REACTION Men's Stretch Slim Fit Finished Bottom Suit", "Dockers Men's Stretch 32 Finished Bottom Suit"};

            if(productList.get(position).getProductLabel().equals("white_shirt")){

                product_name.setText(listOfFruit[random]);
            }
            if(productList.get(position).getProductLabel().equals("suit")){

                product_name.setText(listOfSuit[random]);
            }
            product_price.setText(String.valueOf(random1));
                String imgPath = productList.get(position).getProductUrl();
                if (!TextUtils.isEmpty(imgPath)) {
                    Picasso.with(itemView.getContext())
                            .load(imgPath)
                            .into(ivProductImage);

                }
        }
    }
}
