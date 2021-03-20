package com.visualsearch.finder;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.visualsearch.finder.Fragment.CartFragment;
import com.visualsearch.finder.Fragment.ExploreFragment;
import com.visualsearch.finder.Fragment.HomeFragment;
import com.visualsearch.finder.Fragment.ProductListFragmentTF;
import com.visualsearch.finder.Fragment.ProfileFragment;
import com.visualsearch.finder.Fragment.RecommendFragment;
import com.visualsearch.finder.signup.LoginActivity;
import com.visualsearch.finder.signup.RegisterActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class MainTensorFlowActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintf);
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getTitle());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        ProductListFragmentTF productListFragment = new ProductListFragmentTF();
        productListFragment.setTopResult("all");
//        Bundle bundle = new Bundle();
//        bundle.putString("openCamera", "From Activity");
//// set Fragmentclass Arguments
//        Fragmentclass fragobj = new Fragmentclass();
//        fragobj.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_maintf, productListFragment).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new ExploreFragment();
                            break;
                        case R.id.nav_cart:
                                selectedFragment = new CartFragment();

                            break;
                        case R.id.nav_recommend:
                            selectedFragment = new RecommendFragment();
                            break;
                        case R.id.nav_profile:
                            if (user != null) {
                                selectedFragment = new ProfileFragment();

                            } else {

                            }
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };



}
