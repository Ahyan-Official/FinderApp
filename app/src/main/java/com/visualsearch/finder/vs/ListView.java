package com.visualsearch.finder.vs;

import android.content.Intent;
import android.os.Bundle;

import com.visualsearch.finder.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListView extends AppCompatActivity {

    RecyclerView listView;
    ArrayList<SimilarImages> arrayList;
    ArrayList<String> imageLinks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        imageLinks = new ArrayList<String>();

        Intent i = getIntent();
        imageLinks = i.getStringArrayListExtra("imageLinks");
        //Log.e("oooo", "onCreate: "+imageLinks.get(0) );


        listView =  findViewById(R.id.listView);
        listView.setLayoutManager(new GridLayoutManager(this, 2));

        arrayList = new ArrayList<SimilarImages>();

        for(int j=0;j< imageLinks.size();j++){
            arrayList.add(new SimilarImages(imageLinks.get(j)));
        }

        CustomListAdapter adapter = new CustomListAdapter(
                getApplicationContext(),
                arrayList
        );

        listView.setAdapter(adapter);

    }
}
