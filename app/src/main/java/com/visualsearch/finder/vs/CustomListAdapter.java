package com.visualsearch.finder.vs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visualsearch.finder.R;
import com.visualsearch.finder.products.ProductDetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private ArrayList<SimilarImages> localDataSet;

    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.imageViewProduct);
        }


    }


    public CustomListAdapter(Context context,ArrayList<SimilarImages> dataSet) {
        this.context = context;
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_list_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Picasso.with(context).load(localDataSet.get(position).getImage()).into(viewHolder.textView);

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String categoryId= "024ee472-dda2-4d66-a58c-464ff985a9ec";
                String productDescription= "Place of Origin:\nChina\nBrand Name:\nCustomizatio...";
                String productId= "35864193-185c-4379-ade3-31819a1dfd83";
                String productName= "Men Cycling Yoga Wear Athletics Sports Clothing...";
                String productPrice="$8.75";

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productName",productName);
                intent.putExtra("productId",productId);
                intent.putExtra("productDesc",productDescription);
                intent.putExtra("productPrice",productPrice);
                intent.putExtra("productSale","");
                intent.putExtra("productImage",localDataSet.get(position).getImage());
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("rating",5.0f);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }






}

