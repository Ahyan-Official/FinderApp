package com.visualsearch.finder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.visualsearch.finder.products.AllProductsActivity;
import com.visualsearch.finder.Model.Category;
import com.visualsearch.finder.R;

import java.util.List;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder>
{
    Context context;
    List<Category> categoryList;

    public AllCategoriesAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        holder.categoryName.setText(categoryList.get(position).getCategoryName());
        Glide.with(context).load(categoryList.get(position).getCategoryImage()).into(holder.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AllProductsActivity.class);
                intent.putExtra("categoryId", categoryList.get(position).getCategoryId());
                intent.putExtra("categoryName", categoryList.get(position).getCategoryName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }
}
