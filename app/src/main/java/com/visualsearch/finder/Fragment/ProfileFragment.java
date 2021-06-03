package com.visualsearch.finder.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.visualsearch.finder.ThemeActivity;
import com.visualsearch.finder.address.AddressActivity;
import com.visualsearch.finder.chatbot.MainActivity;
import com.visualsearch.finder.order.OrdersActivity;
import com.visualsearch.finder.profile.ProfileActivity;
import com.visualsearch.finder.R;
import com.visualsearch.finder.signup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {


    ConstraintLayout profileLayout, ordersLayout, addressLayout,chatbot_layout,theme_layout;
    ImageView logout;
    ImageView profile_img,order_img,address_img,chat_im,theme_im;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("PREF_COLOR", getActivity().MODE_PRIVATE);
        int themeSelected = mSharedPreferences.getInt("THEME_SELECTED", R.style.AppTheme);

        getContext().getTheme().applyStyle(themeSelected, true);

        //getActivity().setTheme(themeSelected);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        profileLayout = view.findViewById(R.id.profile_layout);
        ordersLayout = view.findViewById(R.id.order_layout);
        addressLayout = view.findViewById(R.id.address_layout);
        chatbot_layout = view.findViewById(R.id.chatbot_layout);
        theme_layout = view.findViewById(R.id.theme_layout);



        profile_img = view.findViewById(R.id.profile_img);
        order_img = view.findViewById(R.id.order_img);
        address_img = view.findViewById(R.id.address_img);
        chat_im = view.findViewById(R.id.chat_im);
        theme_im = view.findViewById(R.id.theme_im);


        logout = view.findViewById(R.id.logout);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        theme_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThemeActivity.class);
                startActivity(intent);
            }
        });
        chatbot_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        ordersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        
        
        
        //Theme
        if(themeSelected==R.style.AppTheme){
            profile_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
            order_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
            chat_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
            theme_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
            address_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));

        }else if(themeSelected==R.style.AppThemeBlue){
            profile_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeBlue));
            order_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeBlue));
            chat_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeBlue));
            theme_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeBlue));
            address_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeBlue));

        }else if(themeSelected==R.style.AppThemeGreen){
            profile_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeGreen));
            order_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeGreen));
            chat_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeGreen));
            theme_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeGreen));
            address_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeGreen));

        }else if(themeSelected==R.style.AppThemeIndigo){
            profile_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeIndigo));
            order_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeIndigo));
            chat_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeIndigo));
            theme_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeIndigo));
            address_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeIndigo));

        }else if(themeSelected==R.style.AppThemeRed){
            profile_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeRed));
            order_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeRed));
            chat_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeRed));
            theme_im.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeRed));
            address_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.themeRed));

        }
        
        
        
        
        
        
        

        return view;
    }
}
