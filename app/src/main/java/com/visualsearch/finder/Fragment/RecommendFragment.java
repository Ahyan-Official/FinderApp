package com.visualsearch.finder.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.visualsearch.finder.Adapter.CouponAdapter;
import com.visualsearch.finder.Adapter.ProductRecommendAdapter;
import com.visualsearch.finder.Model.Coupon;
import com.visualsearch.finder.Model.ProductsRecommend;
import com.visualsearch.finder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment
{
    RecyclerView recyclerView;
    List<Coupon> couponList;
    CouponAdapter couponAdapter;
    TextView done;

    private List<ProductsRecommend> productsList;
    ProductRecommendAdapter productsAdapter;
    CardView cdInterest;
    CheckBox black,blue,green,yellow,indigo,pink,white;
    CheckBox tshirt,shirt,poloshirt;
    boolean blackk,bluee,greenn,yelloww,indigoo,pinkk,whitee;
    boolean tshirtt,shirtt,poloshirtt;
    ConstraintLayout llmain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        tshirt = view.findViewById(R.id.tshirt);
        shirt = view.findViewById(R.id.shirt);
        poloshirt = view.findViewById(R.id.poloshirt);


        black = view.findViewById(R.id.red);
        blue = view.findViewById(R.id.blue);
        green = view.findViewById(R.id.green);
        yellow = view.findViewById(R.id.yellow);
        white = view.findViewById(R.id.white);
        indigo = view.findViewById(R.id.indigo);
        pink = view.findViewById(R.id.pink);

        llmain = view.findViewById(R.id.llmain);

        done = view.findViewById(R.id.done);
        cdInterest = view.findViewById(R.id.cdInterest);

        recyclerView = view.findViewById(R.id.offer_recycler);
        LinearLayoutManager recommendedlayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(recommendedlayoutManager);

//        couponList = new ArrayList<>();
//        couponAdapter = new CouponAdapter(getContext(), couponList);
//        recyclerView.setAdapter(couponAdapter);




//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Products").startAt()
//                .limitToLast(50);

        //getCoupons();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdInterest.setVisibility(View.GONE);
                llmain.setVisibility(View.VISIBLE);

                if(black.isChecked()){
                    blackk = true;
                }
                if(white.isChecked()){
                    whitee = true;
                }
                if(green.isChecked()){
                    greenn = true;
                }
                if(blue.isChecked()){
                    bluee = true;
                }
                if(indigo.isChecked()){
                    indigoo = true;
                }
                if(pink.isChecked()){
                    pinkk = true;
                }
                //category

                if(shirt.isChecked()){
                    shirtt = true;
                }
                if(tshirt.isChecked()){
                    tshirtt = true;
                }
                if(poloshirt.isChecked()){
                    poloshirtt = true;
                }

                productsList = new ArrayList<>();
                productsAdapter = new ProductRecommendAdapter(getContext(), productsList, blackk,whitee,indigoo,bluee,greenn,pinkk,shirtt,tshirtt,poloshirtt);
                recyclerView.setAdapter(productsAdapter);

                getProducts();

                //Check boxes to get colors

            }
        });
        return view;
    }

    private void getProducts()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ProductsRecommend products = snapshot.getValue(ProductsRecommend.class);
                    if(products.getColors().contains("black")){
                        productsList.add(products);

                    };

                    if (products.getDiscounted().equals("true"))
                    {
                        //discountList.add(products);
                    }
                }
                productsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCoupons()
    {
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Coupon");
        couponRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Coupon coupon = dataSnapshot.getValue(Coupon.class);
                    couponList.add(coupon);
                }
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
