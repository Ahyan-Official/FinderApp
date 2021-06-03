package com.visualsearch.finder;

import akndmr.github.io.colorprefutil.ColorPrefUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.visualsearch.finder.address.AddressActivity;
import com.visualsearch.finder.order.OrdersActivity;
import com.visualsearch.finder.profile.ProfileActivity;

public class ThemeActivity extends AppCompatActivity {

    int selectedBackgroundColorId;

    ImageView profile_img,order_img,address_img,chat_im,theme_im;

    ConstraintLayout profileLayout, ordersLayout, addressLayout,chatbot_layout,theme_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreferences = getSharedPreferences("PREF_COLOR", MODE_PRIVATE);
        int themeSelected = mSharedPreferences.getInt("THEME_SELECTED", R.style.AppTheme);
        setTheme(themeSelected);

        setContentView(R.layout.activity_theme);



        profile_img = findViewById(R.id.profile_img);
        order_img = findViewById(R.id.order_img);
        address_img = findViewById(R.id.address_img);
        chat_im = findViewById(R.id.chat_im);
        theme_im = findViewById(R.id.theme_im);

        profileLayout = findViewById(R.id.profile_layout);
        ordersLayout = findViewById(R.id.order_layout);
        addressLayout = findViewById(R.id.address_layout);
        chatbot_layout = findViewById(R.id.chatbot_layout);
        theme_layout = findViewById(R.id.theme_layout);



        if(themeSelected==R.style.AppTheme){
            profile_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            order_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            chat_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            theme_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue));
            address_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue));

        }else if(themeSelected==R.style.AppThemeBlue){
            profile_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeBlue));
            order_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeBlue));
            chat_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeBlue));
            theme_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeBlue));
            address_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeBlue));

        }else if(themeSelected==R.style.AppThemeGreen){
            profile_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeGreen));
            order_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeGreen));
            chat_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeGreen));
            theme_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeGreen));
            address_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeGreen));

        }else if(themeSelected==R.style.AppThemeIndigo){
            profile_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeIndigo));
            order_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeIndigo));
            chat_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeIndigo));
            theme_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeIndigo));
            address_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeIndigo));

        }else if(themeSelected==R.style.AppThemeRed){
            profile_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeRed));
            order_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeRed));
            chat_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeRed));
            theme_im.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeRed));
            address_img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.themeRed));

        }





        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //rED
                SharedPreferences.Editor editor = getSharedPreferences("PREF_COLOR", MODE_PRIVATE).edit();
                editor.putInt("THEME_SELECTED", R.style.AppThemeRed);
                editor.commit();




                Intent intent = new Intent(getApplicationContext(),com.visualsearch.finder.MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();



            }
        });
        theme_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //blue
                SharedPreferences.Editor editor = getSharedPreferences("PREF_COLOR", MODE_PRIVATE).edit();
                editor.putInt("THEME_SELECTED", R.style.AppThemeBlue);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),com.visualsearch.finder.MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        chatbot_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gree
                SharedPreferences.Editor editor = getSharedPreferences("PREF_COLOR", MODE_PRIVATE).edit();
                editor.putInt("THEME_SELECTED", R.style.AppThemeGreen);
                editor.commit();


                Intent intent = new Intent(getApplicationContext(),com.visualsearch.finder.MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //YELLO
                SharedPreferences.Editor editor = getSharedPreferences("PREF_COLOR", MODE_PRIVATE).edit();
                editor.putInt("THEME_SELECTED", R.style.AppTheme);
                editor.commit();


                Intent intent = new Intent(getApplicationContext(),com.visualsearch.finder.MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //INDIGO
                SharedPreferences.Editor editor = getSharedPreferences("PREF_COLOR", MODE_PRIVATE).edit();
                editor.putInt("THEME_SELECTED", R.style.AppThemeIndigo);
                editor.commit();


                Intent intent = new Intent(getApplicationContext(),com.visualsearch.finder.MainActivity.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });


    }
}