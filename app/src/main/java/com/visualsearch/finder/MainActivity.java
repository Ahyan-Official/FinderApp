package com.visualsearch.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visualsearch.finder.Fragment.CartFragment;
import com.visualsearch.finder.Fragment.ExploreFragment;
import com.visualsearch.finder.Fragment.HomeFragment;
import com.visualsearch.finder.Fragment.RecommendFragment;
import com.visualsearch.finder.Fragment.ProfileFragment;
import com.visualsearch.finder.notification.Token;
import com.visualsearch.finder.signup.LoginActivity;
import com.visualsearch.finder.signup.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    private Dialog signInDialog;
    FirebaseAuth auth;
    FirebaseUser user;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = getSharedPreferences("PREF_COLOR", MODE_PRIVATE);
        int themeSelected = mSharedPreferences.getInt("THEME_SELECTED", R.style.AppTheme);
       setTheme(themeSelected);

        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        floatingActionButton = findViewById(R.id.floatingbtn);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

        if(themeSelected==R.style.AppTheme){

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

        }else if(themeSelected==R.style.AppThemeBlue){

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeBlue)));

        }else if(themeSelected==R.style.AppThemeGreen){

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeGreen)));

        }else if(themeSelected==R.style.AppThemeIndigo){

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeIndigo)));

        }else if(themeSelected==R.style.AppThemeRed){

            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeRed)));

        }
        Button signInDialogBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpDialogBtn = signInDialog.findViewById(R.id.sign_up_btn);

        signInDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        signUpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                editor.putString("showresult", "no");
                editor.commit();
                Intent intent = new Intent(MainActivity.this, MainTensorFlowActivity.class);
                startActivity(intent);





            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

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
                            if (user != null) {
                                selectedFragment = new CartFragment();
                            } else {
                                signInDialog.show();
                            }
                            break;
                        case R.id.nav_recommend:
                            selectedFragment = new RecommendFragment();
                            break;
                        case R.id.nav_profile:
                            if (user != null) {
                                selectedFragment = new ProfileFragment();
                            } else {

                                signInDialog.show();
                            }
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    updateToken(newToken);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
