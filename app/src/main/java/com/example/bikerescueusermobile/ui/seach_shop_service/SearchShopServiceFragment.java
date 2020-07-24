package com.example.bikerescueusermobile.ui.seach_shop_service;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchShopServiceFragment extends BaseFragment implements TopShopSelectedListener, SearchView.OnQueryTextListener {

    @Override
    protected int layoutRes() {
        return R.layout.fragment_search_shop_service;
    }

    @BindView(R.id.rvTopShop)
    RecyclerView mRecyclerView;

    @BindView(R.id.searchViewListAllShopService)
    ListView searchViewListAllShopService;

    @BindView(R.id.searchViewService)
    SearchView searchViewService;

    @BindView(R.id.btnTopOneService)
    Button btnTopOneService;
    @BindView(R.id.btnTopTwoService)
    Button btnTopTwoService;
    @BindView(R.id.btnTopThreeService)
    Button btnTopThreeService;

    @BindView(R.id.homeLoading)
    FrameLayout homeLoading;

    @Inject
    ViewModelFactory viewModelFactory;

    private ShopServiceViewModel viewModel;

    private ArrayList<ShopService> listAllShopServices;
    private ArrayList<ShopService> listTop3Services;

    private ArrayList<Shop> listTop5Shop;

    private ShopServiceListViewAdapter adapter;

    private double distance = -1;
    private String serviceName = "";
    private Shop shop;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopServiceViewModel.class);

        //isLoading => show dialog loading
        observeLoading();

        listTop5Shop = new ArrayList<>();
        listAllShopServices = new ArrayList<>();
        listTop3Services = new ArrayList<>();

        //--------------------------------set up top 5 shop-------------------------------
        viewModel.getAllShop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listShops -> {
                    viewModel.setLoading(false);
                    if (listShops != null) {
                        this.listTop5Shop.addAll(listShops);
                        shop = listTop5Shop.get(0);
                        try {
                            MyMethods.setDistance(listShops);
                        }catch (Exception e) {
                            for (int i = 0; i < listShops.size(); i++) {
                                listShops.get(i).setDistanceFromUser(4);
                            }
                        }
                        mRecyclerView.setAdapter(new TopShopRecyclerViewAdapter(listTop5Shop, this));
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        //        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
                    }
                }, throwable -> {
                    viewModel.setLoading(false);
                    Log.e("SearchShopService", "getTop5Shop: " + throwable.getMessage());
                });

        //-----------------------------------search service---------------------------------------
        viewModel.getAllServices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    viewModel.setLoading(false);
                    if (listServices != null) {
                        listAllShopServices.addAll(listServices);

                        //set nhung dich vu thuong dung`
                        btnTopOneService.setText(listServices.get(0).getName());
                        btnTopTwoService.setText(listServices.get(1).getName());
                        btnTopThreeService.setText(listServices.get(2).getName());
                        this.serviceName = listServices.get(0).getName();
                        for (int i = 0; i < 3; i++){
                            listTop3Services.add(listServices.get(i));
                        }
                    }
                }, throwable -> {
                    viewModel.setLoading(false);
                    Log.e("SearchShopService", "getAllServices: " + throwable.getMessage());
                });

        // Pass results to ListViewAdapter Class
        adapter = new ShopServiceListViewAdapter(getActivity(), new ArrayList<>());

        // Binds the Adapter to the ListView
        searchViewListAllShopService.setAdapter(adapter);

        searchViewService.setOnQueryTextListener(this);
        searchViewService.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchViewListAllShopService.setVisibility(View.VISIBLE);
            } else {
                searchViewListAllShopService.setVisibility(View.GONE);
            }
        });


//        //--------------------------------set up top 3 service-------------------------------
//        listTop3Services = new ArrayList<>();
//        viewModel.getTop3Services()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(list3Services -> {
//                    viewModel.setLoading(false);
//                    if (list3Services != null) {
//                        btnTopOneService.setText(list3Services.get(0).getName());
//                        btnTopTwoService.setText(list3Services.get(1).getName());
//                        btnTopThreeService.setText(list3Services.get(2).getName());
//                        this.serviceName = list3Services.get(0).getName();
//                        listTop3Services.addAll(list3Services);
//                    }
//                }, throwable -> {
//                    viewModel.setLoading(false);
//                    Log.e("SearchShopService", "getTop3Services: " + throwable.getMessage());
//                });

        btnTopOneService.setOnClickListener(v->{
            clusterShopByService("" + btnTopOneService.getText());
        });
        btnTopTwoService.setOnClickListener(v->{
            clusterShopByService("" + btnTopTwoService.getText());
        });
        btnTopThreeService.setOnClickListener(v->{
            clusterShopByService("" + btnTopThreeService.getText());
        });

        // set on list view select ----> set text to the search view
        searchViewListAllShopService.setOnItemClickListener((parent, v, position, id) -> {
            String name = "";
            name = "" + adapter.getItem(position).getName();
            searchViewService.setQuery(name, false);
            searchViewListAllShopService.setVisibility(View.GONE);
        });

    }

    private void observeLoading(){
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    homeLoading.setVisibility(View.VISIBLE);
                } else {
                    homeLoading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void clusterShopByService(String serviceName) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        this.serviceName = serviceName;
        intent.putExtra("serviceName", this.serviceName);
        intent.putExtra("shop", shop);
        intent.putExtra("dis", -1);
        intent.putExtra("listShop", listTop5Shop);
        startActivity(intent);
    }

    @Override
    public void onDetailSelected(Shop shop) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("serviceName", "");
        intent.putExtra("shop", shop);
        distance = shop.getDistanceFromUser();
        intent.putExtra("dis", distance);
        intent.putExtra("listShop", listTop5Shop);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        clusterShopByService("" + searchViewService.getQuery());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText,listAllShopServices);
        if(searchViewListAllShopService.getVisibility() == View.GONE){
            searchViewListAllShopService.setVisibility(View.VISIBLE);
        }
        return false;
    }

}
