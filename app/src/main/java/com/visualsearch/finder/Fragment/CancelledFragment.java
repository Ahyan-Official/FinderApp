package com.visualsearch.finder.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visualsearch.finder.admin.Adapter.OrderAdapter;
import com.visualsearch.finder.Constants;
import com.visualsearch.finder.Model.Order;
import com.visualsearch.finder.R;

import java.util.ArrayList;
import java.util.List;

public class CancelledFragment extends Fragment
{
    RecyclerView recyclerView;
    List<Order> orderList;
    OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelled, container, false);

        recyclerView =view.findViewById(R.id.cancelled_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();

        orderAdapter = new OrderAdapter(getContext(), orderList);
        recyclerView.setAdapter(orderAdapter);

        Constants constants = new Constants();
        String status = "cancelled";
        constants.getOrders(orderAdapter, orderList, status);

        return view;
    }
}