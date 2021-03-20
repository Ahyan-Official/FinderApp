package com.visualsearch.finder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.visualsearch.finder.Model.Cart;
import com.visualsearch.finder.R;
import com.visualsearch.finder.UpdateItemInCart;
import com.visualsearch.finder.cart.CartDataSource;
import com.visualsearch.finder.cart.CartDatabase;
import com.visualsearch.finder.cart.LocalCartDataSource;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<Cart> cartList;
    CartDataSource cartDataSource;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.cartPrice.setText(cartList.get(position).getProduct_price());
        holder.cartName.setText(cartList.get(position).getProduct_name());
        Glide.with(context).load(cartList.get(position).getProduct_image()).into(holder.cartImage);

        holder.cartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartList.get(position).getQuantity() < 10) {
                    cartList.get(position).setQuantity(cartList.get(position).getQuantity() + 1);
                    EventBus.getDefault().postSticky(new UpdateItemInCart(cartList.get(position)));
                }
            }
        });
        holder.cartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartList.get(position).getQuantity() > 1) {
                    cartList.get(position).setQuantity(cartList.get(position).getQuantity() - 1);
                    EventBus.getDefault().postSticky(new UpdateItemInCart(cartList.get(position)));
                }
            }
        });
        holder.cart_qty.setText(cartList.get(position).getQuantity() + "");
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartDataSource.deleteCartItem(cartList.get(position))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
                                notifyItemRemoved(position);
                                EventBus.getDefault().postSticky(new UpdateItemInCart(cartList.get(position)));
                                Toast.makeText(context, "Item Deleted!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        /*holder.wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.wishlistBtn.getTag().equals("add"))
                {
                    holder.wishlistBtn.setImageResource(R.drawable.ic_love_fill);
                    holder.wishlistBtn.setTag("added");
                }
                else
                {
                    holder.wishlistBtn.setImageResource(R.drawable.love);
                    holder.wishlistBtn.setTag("add");
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImage;
        TextView cartName;
        TextView cartPrice;
        ImageView wishlistBtn;
        ImageView deleteBtn;
        ImageView cartPlus;
        ImageView cartMinus;
        TextView cart_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImage = itemView.findViewById(R.id.cart_image);
            cartName = itemView.findViewById(R.id.cart_name);
            cartPrice = itemView.findViewById(R.id.cart_price);
            wishlistBtn = itemView.findViewById(R.id.add_to_wishlist);
            deleteBtn = itemView.findViewById(R.id.delete_cart);
            cartPlus = itemView.findViewById(R.id.cart_add);
            cartMinus = itemView.findViewById(R.id.cart_minus);
            cart_qty = itemView.findViewById(R.id.cart_quantity);
        }
    }
}