package com.visualsearch.finder.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visualsearch.finder.Adapter.CartAdapter;
import com.visualsearch.finder.Constants;
import com.visualsearch.finder.Model.Cart;
import com.visualsearch.finder.R;
import com.visualsearch.finder.UpdateItemInCart;
import com.visualsearch.finder.address.ShiptoActivity;
import com.visualsearch.finder.cart.CartDataSource;
import com.visualsearch.finder.cart.CartDatabase;
import com.visualsearch.finder.cart.CartViewModal;
import com.visualsearch.finder.cart.LocalCartDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment {
    private RecyclerView cartRecyclerView;
    CartAdapter adapter;
    private Button next;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextView subtotal;
    private TextView items;
    private TextView shipping;
    private TextView totalPrice;
    /*private TextView coupontxt;
    private EditText couponEt;
    private Button couponBtn;
    private ImageView remove_coupon;*/

    private Dialog dialog;

    private ConstraintLayout empty, full;

    private Parcelable recyclerViewState;
    private CartDataSource cartDataSource;
    private CartViewModal cartViewModal;
    String price, items_st;
    String delivery = "0";
    double subtotalPrice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cartViewModal = ViewModelProviders.of(this).get(CartViewModal.class);
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecycler);
        next = view.findViewById(R.id.checkout_btn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        delivery = Constants.DELIVERY_PRICE;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(layoutManager);

        cartViewModal.initCardDataSource(getContext());
        cartViewModal.getMutableLiveDataCart().observe(getViewLifecycleOwner(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                if (carts == null || carts.isEmpty()) {
                    onEmptyCart();
                } else {
                    adapter = new CartAdapter(getContext(), carts);
                    cartRecyclerView.setAdapter(adapter);
                }
            }
        });

        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());

        subtotal = view.findViewById(R.id.subtotal_price);
        items = view.findViewById(R.id.items);
        shipping = view.findViewById(R.id.shipping_price);
        totalPrice = view.findViewById(R.id.total_price);
        empty = view.findViewById(R.id.empty_cart_layout);
        //coupontxt = view.findViewById(R.id.coupon_applied_txt);
        full = view.findViewById(R.id.order_details_layout);
        /*couponBtn = view.findViewById(R.id.coupon_bn);
        couponEt = view.findViewById(R.id.coupon_edittext);
        remove_coupon = view.findViewById(R.id.remove_coupon);*/

        shipping.setText(delivery);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView msg = dialog.findViewById(R.id.msg);
        Button deleteBtn = dialog.findViewById(R.id.delete_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);

        msg.setText("Are You sure you want to remove Coupon?");
        deleteBtn.setText("Remove");
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Cart")
                        .child(user.getUid())
                        .child("couponCode")
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Coupon Removed Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShiptoActivity.class);
                intent.putExtra("items", items.getText().toString());
                intent.putExtra("subtotal", subtotal.getText().toString());
                intent.putExtra("shipping", shipping.getText().toString());
                intent.putExtra("total", totalPrice.getText().toString());
                startActivity(intent);
            }
        });

        /*couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(couponEt.getText().toString())) {
                    couponBtn.setError("Field is Required!");
                } else {
                    couponBtn.setVisibility(View.INVISIBLE);
                    validateCoupon();
                }
            }
        });

        remove_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        getCouponStatus();*/
        sumAllItemsInCart();
        return view;
    }

    private void sumAllItemsInCart() {
        cartDataSource.sumPriceInCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Double aDouble) {
                        price = "" + aDouble;
                        subtotalPrice = aDouble;
                        cartDataSource.countItemsInCart()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@io.reactivex.annotations.NonNull Integer aInteger) {
                                        items_st = aInteger + "Items";
                                        subtotal.setText(price);
                                        items.setText("Items(" + aInteger + ")");
                                        double total = Integer.parseInt(delivery) + aDouble;
                                        totalPrice.setText(String.valueOf(total));
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                        onEmptyCart();
                                    }
                                });
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        onEmptyCart();
                    }
                });
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.onStart();
    }

    private void onEmptyCart() {
        empty.setVisibility(View.VISIBLE);
        full.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
    }

    /*private void getCouponStatus() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Promo")
                .child(user.getUid())
                .child("code");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() != null) {
                    if (snapshot.exists()) {
                        String couponApplied = snapshot.getValue(String.class);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coupon")
                                .child(couponApplied);

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Coupon coupon = snapshot.getValue(Coupon.class);
                                String couponMin = coupon.getCouponMin();
                                if (coupon.getCouponMin() != null && (int) subtotalPrice > Integer.parseInt(coupon.getCouponMin())) {
                                    FirebaseDatabase.getInstance().getReference("Promo")
                                            .child(user.getUid())
                                            .child("code")
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            remove_coupon.setVisibility(View.GONE);
                                            coupontxt.setTextColor(getActivity().getResources().getColor(R.color.red));
                                            coupontxt.setText("Minimum order price should be " + couponMin);
                                            coupontxt.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } else {
                                    remove_coupon.setVisibility(View.VISIBLE);
                                    coupontxt.setTextColor(getActivity().getResources().getColor(R.color.blue));
                                    coupontxt.setText("Coupon '" + couponApplied + "' has been applied!");
                                    coupontxt.setVisibility(View.VISIBLE);
                                    int couponAfter = (int) subtotalPrice - Integer.parseInt(coupon.getCouponPrice());
                                    subtotal.setText(String.valueOf(couponAfter));
                                    int totalCouponAfter = couponAfter + Integer.parseInt(delivery);
                                    totalPrice.setText(String.valueOf(totalCouponAfter));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        coupontxt.setVisibility(View.GONE);
                        remove_coupon.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void validateCoupon() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Promo")
                .child(user.getUid())
                .child("code");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getContext(), "A coupon is already applied!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coupon")
                            .child(couponEt.getText().toString().toUpperCase());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Coupon coupon = snapshot.getValue(Coupon.class);
                                String couponCode = coupon.getCouponCode();
                                String couponMin = coupon.getCouponMin();
                                if (couponMin != null && (int) subtotalPrice < Integer.parseInt(couponMin)) {
                                    couponBtn.setVisibility(View.VISIBLE);
                                    coupontxt.setTextColor(getActivity().getResources().getColor(R.color.red));
                                    coupontxt.setText("Minimum order price should be " + coupon.getCouponMin());
                                    coupontxt.setVisibility(View.VISIBLE);
                                    remove_coupon.setVisibility(View.GONE);
                                    couponEt.setText("");
                                    adapter.notifyDataSetChanged();
                                } else {
                                    FirebaseDatabase.getInstance().getReference("Promo")
                                            .child(user.getUid())
                                            .child("code")
                                            .setValue(coupon.getCouponCode())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    couponBtn.setVisibility(View.VISIBLE);
                                                    coupontxt.setTextColor(getActivity().getResources().getColor(R.color.blue));
                                                    coupontxt.setText("Coupon '" + couponCode + "' has been applied!");
                                                    coupontxt.setVisibility(View.VISIBLE);
                                                    remove_coupon.setVisibility(View.VISIBLE);
                                                    couponEt.setText("");
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                            } else {
                                couponBtn.setVisibility(View.VISIBLE);
                                coupontxt.setTextColor(getActivity().getResources().getColor(R.color.red));
                                coupontxt.setText("Coupon is incorrect!");
                                coupontxt.setVisibility(View.VISIBLE);
                                remove_coupon.setVisibility(View.GONE);
                                couponEt.setText("");
                            }
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
    }*/

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateItemInCartEvent(UpdateItemInCart event) {
        if (event.getCart() != null) {
            recyclerViewState = cartRecyclerView.getLayoutManager().onSaveInstanceState();
            cartDataSource.updateCartItem(event.getCart())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
                            calculateTotalPrice();
                            cartRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Toast.makeText(getContext(), "Error[Event]: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void calculateTotalPrice() {
        cartDataSource.sumPriceInCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Double aDouble) {
                        price = "" + aDouble;
                        subtotalPrice = aDouble;
                        cartDataSource.countItemsInCart()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@io.reactivex.annotations.NonNull Integer aInteger) {
                                        items_st = aInteger + " Items";
                                        subtotal.setText(price);
                                        items.setText("Items(" + aInteger + ")");
                                        double total = Integer.parseInt(delivery) + aDouble;
                                        totalPrice.setText(String.valueOf(total));
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(getContext(), "Error[Sum]: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
