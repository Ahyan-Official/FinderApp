package com.visualsearch.finder.cart;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.visualsearch.finder.Model.Cart;
import com.visualsearch.finder.Model.Wishlist;
import com.visualsearch.finder.wishlist.WishlistDAO;

@Database(version = 1, entities = {Cart.class, Wishlist.class}, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();

    public abstract WishlistDAO wishlistDAO();

    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, "UnnatiShoppingCartDB1").build();
        return instance;
    }
}
