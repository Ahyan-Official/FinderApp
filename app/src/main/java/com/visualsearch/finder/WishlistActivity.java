package com.visualsearch.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.visualsearch.finder.Adapter.WishlistAdapter;
import com.visualsearch.finder.Model.Wishlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Wishlist> wishlistList;
    WishlistAdapter wishlistAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.wishlistReycler);
        back = findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(WishlistActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);

        wishlistList = new ArrayList<>();
        wishlistAdapter = new WishlistAdapter(WishlistActivity.this, wishlistList);
        recyclerView.setAdapter(wishlistAdapter);

        getLikes();

    }

    private void getLikes()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes")
                .child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishlistList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Wishlist wishlist = snapshot.getValue(Wishlist.class);
                    wishlistList.add(wishlist);
                }
                wishlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
