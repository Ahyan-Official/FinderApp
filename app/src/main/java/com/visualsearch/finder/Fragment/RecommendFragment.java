package com.visualsearch.finder.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visualsearch.finder.Adapter.CouponAdapter;
import com.visualsearch.finder.Model.Coupon;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        recyclerView = view.findViewById(R.id.offer_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        couponList = new ArrayList<>();
        couponAdapter = new CouponAdapter(getContext(), couponList);
        recyclerView.setAdapter(couponAdapter);

        getCoupons();
        return view;
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
