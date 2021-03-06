package com.visualsearch.finder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.visualsearch.finder.Model.Products;
import com.visualsearch.finder.Model.ProductsRecommend;
import com.visualsearch.finder.Model.Review;
import com.visualsearch.finder.R;
import com.visualsearch.finder.products.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductRecommendAdapter extends RecyclerView.Adapter<ProductRecommendAdapter.ViewHolder> {
    private Context context;
    private List<ProductsRecommend> productsList;
    boolean redd,bluee,greenn,yelloww,indigoo,pinkk,whitee;
    boolean tshirtt,shirtt,poloshirtt;

    public ProductRecommendAdapter(Context context, List<ProductsRecommend> productsList, boolean redd, boolean whitee, boolean indigoo, boolean bluee, boolean greenn, boolean pinkk, boolean shirtt, boolean tshirtt, boolean poloshirtt) {
        this.context = context;
        this.productsList = productsList;
        this.redd = redd;
        this.bluee = bluee;
        this.greenn = greenn;
        this.yelloww = yelloww;
        this.indigoo = indigoo;
        this.pinkk = pinkk;
        this.whitee = whitee;
        this.tshirtt = tshirtt;
        this.shirtt = shirtt;
        this.poloshirtt = poloshirtt;
    }

    public ProductRecommendAdapter(Context context, List<ProductsRecommend> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        if(redd && productsList.get(position).getColors().equals("red")){
//
//            holder.itemView.setVisibility(View.VISIBLE);
//
//
//        }else {
//            holder.itemView.setVisibility(View.GONE);
//
//        }
        holder.productName.setText(productsList.get(position).getProductName());
        Glide.with(context).load(productsList.get(position).getProductImage()).into(holder.productImage);
        holder.productBeforeDicountPrice.setText(productsList.get(position).getProductDiscount());
        holder.productDiscountRatio.setText(productsList.get(position).getProductPercent() + "% Off");
        holder.productPrice.setText(productsList.get(position).getProductPrice() + ".00");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productName", productsList.get(position).getProductName());
                intent.putExtra("productId", productsList.get(position).getProductId());
                intent.putExtra("productDesc", productsList.get(position).getProductDescription());
                intent.putExtra("productPrice", productsList.get(position).getProductPrice());
                intent.putExtra("productImage", productsList.get(position).getProductImage());
                intent.putExtra("categoryId", productsList.get(position).getCategoryId());
                intent.putExtra("rating", productsList.get(position).getRating());

                context.startActivity(intent);
            }
        });



//        if (productsList.get(position).getDiscounted().equals("true")) {
//            holder.currency_discounted.setVisibility(View.VISIBLE);
//            holder.discountDivider.setVisibility(View.VISIBLE);
//            holder.productBeforeDicountPrice.setVisibility(View.VISIBLE);
//            holder.productDiscountRatio.setVisibility(View.VISIBLE);
//        } else {
//            holder.currency_discounted.setVisibility(View.INVISIBLE);
//            holder.discountDivider.setVisibility(View.INVISIBLE);
//            holder.productBeforeDicountPrice.setVisibility(View.INVISIBLE);
//            holder.productDiscountRatio.setVisibility(View.INVISIBLE);
//        }

        getRating(holder.ratingBar, productsList.get(position).getProductId());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        RelativeLayout discountDivider;
        TextView productPrice;
        TextView productBeforeDicountPrice;
        TextView productDiscountRatio;
        TextView currency_discounted;
        TextView currency;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productBeforeDicountPrice = itemView.findViewById(R.id.product_before_discount_price);
            discountDivider = itemView.findViewById(R.id.discount_divider);
            productDiscountRatio = itemView.findViewById(R.id.discount_ratio);
            currency_discounted = itemView.findViewById(R.id.currency_discounted);
            currency = itemView.findViewById(R.id.currency);
            ratingBar = itemView.findViewById(R.id.product_rating);
        }
    }

    private void getRating(final RatingBar ratingBar, String product_id) {
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("Ratings")
                .child(product_id);

        ratingRef.addValueEventListener(new ValueEventListener() {
            int count = 0;
            int sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        ratingBar.setVisibility(View.VISIBLE);
                        Review review = dataSnapshot.getValue(Review.class);
                        sum += review.getRating();
                        count++;
                        int average = sum / count;
                        ratingBar.setRating(average);
                    } else {
                        ratingBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}