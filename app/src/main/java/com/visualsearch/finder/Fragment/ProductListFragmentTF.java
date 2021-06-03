package com.visualsearch.finder.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visualsearch.finder.Adapter.ProductAdapterTF;
import com.visualsearch.finder.Interface.RetrofitInterface;
import com.visualsearch.finder.R;
import com.visualsearch.finder.Utils.APIClient;
import com.visualsearch.finder.products.ProductTF;
import com.visualsearch.finder.products.ProductsTF;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ProductListFragmentTF extends Fragment {

    RecyclerView.LayoutManager topLayoutManager, secondLayoutManager;
    RecyclerView rvTopProducts, rvSecondProducts;
    public RetrofitInterface retrofitInterface;
    TextView tvProductCategory, tvSecondCategory;
    String topResult = null, secondResult=null;
    Boolean mSimilarItems = false;
    FloatingActionButton fabButtonOpenCamera;

    public ProductListFragmentTF() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        rvTopProducts = rootView.findViewById(R.id.rvProducts);
        rvSecondProducts = rootView.findViewById(R.id.rvSecondProducts);




        tvProductCategory = rootView.findViewById(R.id.tvProductCategory);
        tvSecondCategory = rootView.findViewById(R.id.tvSecondCategory);
        if(mSimilarItems){
            tvProductCategory.setText("View similar products");
            tvSecondCategory.setText("You can also view");
          //  if(!secondResult.equalsIgnoreCase("all"))

        }
        fabButtonOpenCamera = rootView.findViewById(R.id.btnDetectObject);
        fabButtonOpenCamera.setVisibility(View.VISIBLE);




        fabButtonOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragment cameraFragment = new CameraFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_maintf, cameraFragment)
                        .commit();

                fabButtonOpenCamera.setVisibility(View.GONE);
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String showresult = prefs.getString("showresult", "no");


        if(showresult!=null && showresult.equals("yes")){



        }else if(showresult!=null && showresult.equals("no")){

            CameraFragment cameraFragment = new CameraFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_maintf, cameraFragment)
                    .commit();
        }



        setupRecyclerView();
        return rootView;
    }

    private void setupRecyclerView() {
        topLayoutManager = new GridLayoutManager(getActivity(), 2);
        secondLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvTopProducts.setLayoutManager(topLayoutManager);
        rvSecondProducts.setLayoutManager(secondLayoutManager);
    }

    private void loadProductImage(final String topResultArg) {


        Log.e("topres", "loadProductImage: "+topResultArg );
        retrofitInterface = APIClient.getClient().create(RetrofitInterface.class);

        Call<ProductsTF> call = retrofitInterface.getProductList();
        call.enqueue(new Callback<ProductsTF>() {
            @Override
            public void onResponse(Call<ProductsTF> call, Response<ProductsTF> response) {
                ProductsTF products = response.body();
                ArrayList<ProductTF> customProducts;
                customProducts = new ArrayList();
                for(int i = 0; i< products.getProducts().size(); i++){
                    if (topResultArg.equalsIgnoreCase("all")){
                        customProducts = products.getProducts();
                        break;
                    }
                    else if(products.getProducts().get(i).getProductLabel().equalsIgnoreCase(topResultArg))
                    {
                        customProducts.add(products.getProducts().get(i));
                    }
                }
                if(topResultArg.equalsIgnoreCase("none")){
                    Toast.makeText(getContext() ,"No similar items available!", Toast.LENGTH_SHORT).show();
                    rvTopProducts.setAdapter(new ProductAdapterTF(products.getProducts(), false));
                }else{
                    rvTopProducts.setAdapter(new ProductAdapterTF(customProducts, false));
                }
            }

            @Override
            public void onFailure(Call<ProductsTF> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void loadSecondResultsImage(final String secondResultArg) {

        retrofitInterface = APIClient.getClient().create(RetrofitInterface.class);

        Call<ProductsTF> call = retrofitInterface.getProductList();
        call.enqueue(new Callback<ProductsTF>() {
            @Override
            public void onResponse(Call<ProductsTF> call, Response<ProductsTF> response) {
                ProductsTF products = response.body();
                ArrayList<ProductTF> customProducts;
                customProducts = new ArrayList();
                for(int i = 0; i< products.getProducts().size(); i++){
                    if(products.getProducts().get(i).getProductLabel().equalsIgnoreCase(secondResultArg)){
                        customProducts.add(products.getProducts().get(i));
                    }
                }
                if(!secondResultArg.equalsIgnoreCase("all"))
                    rvSecondProducts.setAdapter(new ProductAdapterTF(customProducts, true));
                else
                    rvSecondProducts.setAdapter(new ProductAdapterTF(products.getProducts(), true));

            }

            @Override
            public void onFailure(Call<ProductsTF> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void setTopResult(String result) {
        topResult = result;
        topResult = "suit ";
        loadProductImage(topResult);
    }

    public void setSecondResult(String result) {
        secondResult = result;
        loadSecondResultsImage(secondResult);
    }

    public void setSimilarItems(boolean similarItems) {
        mSimilarItems = similarItems;
    }
}
