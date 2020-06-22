package com.example.bikerescueusermobile.ui.seach_shop_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.util.ViewModelFactory;

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
    @Inject
    ViewModelFactory viewModelFactory;

    private ShopServiceViewModel viewModel;

    private List<ShopService> listAllShopServices;
    private List<ShopService> listTop3Services;

    private List<Shop> listTop5Shop;

    private ShopServiceListViewAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Shop s1 = new Shop(1, "Sửa xe Tiến Đạt", "100 Phan Huy Ích, Tân Hòa, Gò Vấp", "4");
        Shop s2 = new Shop(2, "Tiệm Sửa Xe Phúc Tài", "169B, Nguyễn Chí Thanh, Phường 12, Quận 5, Phường 6, Quận 10, Hồ Chí Minh, Việt Nam", "3.5");
        Shop s3 = new Shop(3, "Tiệm Sửa Xe Ty TN", "504 Ngô Gia Tự, Phường 9, Quận 5, Hồ Chí Minh, Việt Nam", "0");
        Shop s4 = new Shop(4, "Tiệm Sửa Xe Minh Tuấn", "533 Lê Hồng Phong, Phường 10, Quận 10, Hồ Chí Minh, Việt Nam", "5");

        List<Shop> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        s1.setShopRatingStar("2.0");
        list.add(s1);
        list.add(s2);
        s2.setShopRatingStar("4.5");
        list.add(s3);
        list.add(s4);


//        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new TopShopRecyclerViewAdapter(list, this, this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //-----------------------------------search service---------------------------------------
        listAllShopServices = new ArrayList<>();
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopServiceViewModel.class);
        viewModel.getAllServices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    if (listServices != null) {
                        listAllShopServices.addAll(listServices);
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
                    }
                }, throwable -> {
                    Log.e("SearchShopService", "getAllServices: " + throwable.getMessage());
                });


        //--------------------------------set up top 3 service-------------------------------
        listTop3Services = new ArrayList<>();
        viewModel.getTop3Services()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list3Services -> {
                    if (list3Services != null) {
                        btnTopOneService.setText(list3Services.get(0).getName());
                        btnTopTwoService.setText(list3Services.get(1).getName());
                        btnTopThreeService.setText(list3Services.get(2).getName());
                        listTop3Services.addAll(list3Services);
                    }
                }, throwable -> {
                    Log.e("SearchShopService", "getTop3Services: " + throwable.getMessage());
                });

        //--------------------------------set up top 5 shop-------------------------------
        listTop5Shop = new ArrayList<>();
        viewModel.getTop5Shop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listTop5Shop -> {
                    if (listTop5Shop != null) {
                        this.listTop5Shop.addAll(listTop5Shop);
                        Log.e("SearchShopService", "getTop5Shop: size" + listTop5Shop.size() + " --- tostring" + listTop5Shop.toString());

                    }
                }, throwable -> {
                    Log.e("SearchShopService", "getTop5Shop: " + throwable.getMessage());
                });

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query,listAllShopServices);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText,listAllShopServices);
        return false;
    }

    @Override
    public void onDetailSelected(Shop shop) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
//        intent.putExtra("caseCode", feed.getCaseCode());
        startActivity(intent);
    }
}
