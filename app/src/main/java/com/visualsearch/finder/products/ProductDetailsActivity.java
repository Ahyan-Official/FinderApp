package com.visualsearch.finder.products;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.visualsearch.finder.Adapter.ProductsAdapter;
import com.visualsearch.finder.Model.Cart;
import com.visualsearch.finder.Model.Products;
import com.visualsearch.finder.Model.Review;
import com.visualsearch.finder.Model.User;
import com.visualsearch.finder.Model.Wishlist;
import com.visualsearch.finder.R;
import com.visualsearch.finder.SearchActivity;
import com.visualsearch.finder.cart.CartDataSource;
import com.visualsearch.finder.cart.CartDatabase;
import com.visualsearch.finder.cart.LocalCartDataSource;
import com.visualsearch.finder.review.AllReviewsActivity;
import com.visualsearch.finder.signup.LoginActivity;
import com.visualsearch.finder.signup.RegisterActivity;
import com.visualsearch.finder.wishlist.LocalWishlistDataSource;
import com.visualsearch.finder.wishlist.WishlistDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView productImage;
    ImageView ratingImage;
    TextView productName;
    TextView productDesc;
    TextView productPrice;
    TextView currency;
    ImageView wishlistBtn;
    Button addToCart;
    ImageView back;
    TextView more_reviews;
    TextView review_name;
    ImageView search_btn;
    TextView total_ratings_number;
    TextView reviewTxt;
    TextView review_date;
    TextView no_review;
    TextView also_like_recycler_txt;
    TextView product_title;

    RecyclerView alsoRecycler;
    List<Products> productsList;
    ProductsAdapter productsAdapter;

    String product_name;
    String product_id;
    String product_image;
    String product_desc;
    String product_price;
    String product_sale;
    String category_id;

    FirebaseAuth auth;
    FirebaseUser user;

    private RatingBar productRating, totalRating, reviewRating;

    private Dialog signInDialog;

    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    private WishlistDataSource wishlistDataSource;

    Wishlist wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        final Intent intent = getIntent();
        product_name = intent.getStringExtra("productName");
        product_id = intent.getStringExtra("productId");
        product_desc = intent.getStringExtra("productDesc");
        product_price = intent.getStringExtra("productPrice");
        product_sale = intent.getStringExtra("productSale");
        product_image = intent.getStringExtra("productImage");
        category_id = intent.getStringExtra("categoryId");

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDesc = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        currency = findViewById(R.id.currency);
        alsoRecycler = findViewById(R.id.also_like_recyclerView);
        wishlistBtn = findViewById(R.id.add_to_wishlist);
        addToCart = findViewById(R.id.add_to_cart_btn);
        back = findViewById(R.id.back_btn);
        more_reviews = findViewById(R.id.more_reviews);
        search_btn = findViewById(R.id.search_btn);
        productRating = findViewById(R.id.rating_bar);
        reviewRating = findViewById(R.id.review_rating);
        totalRating = findViewById(R.id.ratings_total);
        ratingImage = findViewById(R.id.review_image);
        review_name = findViewById(R.id.review_name);
        total_ratings_number = findViewById(R.id.total_ratings_number);
        reviewTxt = findViewById(R.id.review);
        review_date = findViewById(R.id.review_date);
        no_review = findViewById(R.id.no_review);
        also_like_recycler_txt = findViewById(R.id.also_like_recycler_txt);
        product_title = findViewById(R.id.product_title);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(ProductDetailsActivity.this).cartDAO());
        wishlistDataSource = new LocalWishlistDataSource(CartDatabase.getInstance(ProductDetailsActivity.this).wishlistDAO());


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailsActivity.this, SearchActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        more_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ratingIntent = new Intent(ProductDetailsActivity.this, AllReviewsActivity.class);
                ratingIntent.putExtra("productId", product_id);
                startActivity(ratingIntent);
            }
        });

        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInDialogBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpDialogBtn = signInDialog.findViewById(R.id.sign_up_btn);

        signInDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
            }
        });
        signUpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                startActivity(new Intent(ProductDetailsActivity.this, RegisterActivity.class));
            }
        });

        isLiked();

        wishlist = new Wishlist();
        wishlist.setProduct_id(product_id);
        wishlist.setProduct_image(product_image);
        wishlist.setProduct_name(product_name);
        wishlist.setProduct_price(product_price);
        wishlist.setSale_price(product_sale);

        wishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wishlistBtn.getTag().equals("Like")) {
                    compositeDisposable.add(wishlistDataSource.insertOrReplaceAll(wishlist)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                wishlistBtn.setTag("Liked");
                                wishlistBtn.setImageResource(R.drawable.ic_love_fill);
                            }, throwable -> {
                                Toast.makeText(ProductDetailsActivity.this, "Error[WISHLIST_ADD]: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }));

                } else if (wishlistBtn.getTag().equals("Liked")) {
                    wishlistDataSource.deleteWishlistItem(wishlist)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
                                    wishlistBtn.setTag("Like");
                                    wishlistBtn.setImageResource(R.drawable.ic_fav);
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                    Toast.makeText(ProductDetailsActivity.this, "Error[WISHLIST_REMOVE]: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }


        });

        productName.setText(product_name);
        product_title.setText(product_name);
        productPrice.setText(product_price);
        productDesc.setText(product_desc);
        Glide.with(getApplicationContext()).load(product_image).into(productImage);

        RecyclerView.LayoutManager alsoLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        alsoRecycler.setLayoutManager(alsoLayoutManager);

        productsList = new ArrayList<>();

        productsAdapter = new ProductsAdapter(ProductDetailsActivity.this, productsList);
        alsoRecycler.setAdapter(productsAdapter);


        getRating();
        getSingleReview();
        getAlsoProducts();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart();
                cart.setProduct_id(product_id);
                cart.setQuantity(1);
                cart.setProduct_image(product_image);
                cart.setProduct_price(product_price);
                cart.setProduct_name(product_name);

                cartDataSource.getItemInCart(product_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Cart>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull Cart cartFromDB) {
                                if (cartFromDB.equals(cart)) {
                                    cartFromDB.setQuantity(cartFromDB.getQuantity() + cart.getQuantity());
                                    cartDataSource.updateCartItem(cartFromDB)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new SingleObserver<Integer>() {
                                                @Override
                                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
                                                    Toast.makeText(ProductDetailsActivity.this, "Cart Updated!", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                                    Toast.makeText(ProductDetailsActivity.this, "UPDATE ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cart)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                            }, throwable -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            }));

                                }

                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                if (e.getMessage().contains("empty")) {
                                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cart)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                            }, throwable -> {
                                                Toast.makeText(ProductDetailsActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            }));
                                } else
                                    Toast.makeText(ProductDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                compositeDisposable.add(cartDataSource.insertOrReplaceAll(cart)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {

                        }));
            }
        });


    }

    private void isLiked() {
        wishlistDataSource.getItemInWishlist(product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Wishlist>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Wishlist wishlistFromDB) {
                        if (wishlistFromDB.equals(wishlist)) {
                            wishlistBtn.setImageResource(R.drawable.ic_love_fill);
                            wishlistBtn.setTag("Liked");
                        } else {
                            wishlistBtn.setImageResource(R.drawable.ic_fav);
                            wishlistBtn.setTag("Like");
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        wishlistBtn.setTag("Like");
                        wishlistBtn.setImageResource(R.drawable.ic_fav);
                    }
                });
    }

    private void getAlsoProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    if (products.getCategoryId().equals(category_id)) {
                        if (!products.getProductId().equals(product_id)) {
                            productsList.add(products);
                        }
                    }
                }
                productsAdapter.notifyDataSetChanged();
                if (productsAdapter.getItemCount() == 0) {
                    also_like_recycler_txt.setVisibility(View.INVISIBLE);
                } else {
                    also_like_recycler_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSingleReview() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings")
                .child(product_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Review review = dataSnapshot.getValue(Review.class);
                    reviewRating.setRating(review.getRating());
                    reviewTxt.setText(review.getReview());
                    review_date.setText(review.getDate());

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                            .child(review.getUserId());

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            review_name.setText(user.getFullname());
                            Glide.with(ProductDetailsActivity.this).load(user.getImageurl()).into(ratingImage);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getRating() {
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("Ratings")
                .child(product_id);

        ratingRef.addValueEventListener(new ValueEventListener() {
            int count = 0;
            int sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        Review review = dataSnapshot.getValue(Review.class);
                        sum += review.getRating();
                        count++;

                        no_review.setVisibility(View.GONE);
                        ratingImage.setVisibility(View.VISIBLE);
                        totalRating.setVisibility(View.VISIBLE);
                        reviewRating.setVisibility(View.VISIBLE);
                        review_name.setVisibility(View.VISIBLE);
                        reviewTxt.setVisibility(View.VISIBLE);
                        total_ratings_number.setVisibility(View.VISIBLE);
                        review_date.setVisibility(View.VISIBLE);
                    } else {
                        no_review.setVisibility(View.VISIBLE);
                        ratingImage.setVisibility(View.GONE);
                        totalRating.setVisibility(View.GONE);
                        reviewRating.setVisibility(View.GONE);
                        review_name.setVisibility(View.GONE);
                        reviewTxt.setVisibility(View.GONE);
                        total_ratings_number.setVisibility(View.GONE);
                        review_date.setVisibility(View.GONE);
                        productRating.setRating(0);
                        totalRating.setRating(0);
                    }
                }
                if (count != 0) {
                    int average = sum / count;
                    productRating.setRating(average);
                    totalRating.setRating(average);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
