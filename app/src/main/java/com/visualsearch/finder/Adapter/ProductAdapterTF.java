package com.visualsearch.finder.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;
import com.visualsearch.finder.R;
import com.visualsearch.finder.products.ProductTF;

import java.util.ArrayList;

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

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }

        public void bind(final int position) {
                String imgPath = productList.get(position).getProductUrl();
                if (!TextUtils.isEmpty(imgPath)) {
                    Picasso.with(itemView.getContext())
                            .load(imgPath)
                            .into(ivProductImage);

                }
        }
    }
}
