package com.example.bikerescueusermobile.ui.seach_shop_service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.example.bikerescueusermobile.data.model.user.UserLatLong;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchShopServiceFragment extends BaseFragment implements TopShopSelectedListener, SearchView.OnQueryTextListener {

    @Override
    protected int layoutRes() {
        return R.layout.fragment_search_shop_service;
    }

    private static final String TAG = "SearchShopServiceFragment";

    @BindView(R.id.rvTopShop)
    RecyclerView mRecyclerView;

    @BindView(R.id.searchViewListAllShopService)
    ListView searchViewListAllShopService;

    @BindView(R.id.searchViewService)
    SearchView searchViewService;

    @BindView(R.id.txtMoreService)
    TextView txtMoreService;

    @BindView(R.id.btnTopOneService)
    Button btnTopOneService;

    @BindView(R.id.btnTopTwoService)
    Button btnTopTwoService;

    @BindView(R.id.homeLoading)
    FrameLayout homeLoading;

    @Inject
    ViewModelFactory viewModelFactory;

    private ShopServiceViewModel viewModel;

    private ArrayList<ShopService> listAllShopServices;


    private ShopServiceListViewAdapter adapter;

    private double distance = -1;
    private String serviceName = "";
    private Shop shop;
    private final ArrayList<Shop> shops = new ArrayList<>();

    @SuppressLint("LongLogTag")
    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopServiceViewModel.class);

        //isLoading => show dialog loading
        observeLoading();

        listAllShopServices = new ArrayList<>();

        //--------------------------------set up all shop-------------------------------
        final FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                CurrentUser.getInstance().setLatitude("" + location.getLatitude());
                CurrentUser.getInstance().setLongtitude("" + location.getLongitude());

                viewModel.getAllShop()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listShops -> {
                            viewModel.setLoading(false);
                            if (listShops != null && listShops.size() > 0) {
                                shop = listShops.get(0);
                                try {
                                    MyMethods.setDistance(listShops);

                                    //bubble sort
                                    List<Shop> sortedList = new ArrayList<>();
                                    sortedList.addAll(listShops);
                                    int size = sortedList.size();
                                    for (int i = 0; i < size - 1; i++)
                                        for (int j = 0; j < size - i - 1; j++)
                                            if (sortedList.get(j).getDistanceFromUser() > sortedList.get(j + 1).getDistanceFromUser()) {
                                                // swap arr[j+1] and arr[j]
                                                Shop temp = sortedList.get(j);
                                                sortedList.set(j, sortedList.get(j + 1));
                                                sortedList.set(j + 1, temp);
                                            }

                                    this.shops.addAll(sortedList);
                                    mRecyclerView.setAdapter(new TopShopRecyclerViewAdapter(sortedList, this));
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    //        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
                                } catch (Exception e) {
                                    Log.e(TAG, "cannot generate distances: " + e.getMessage());
                                }
                            }
                        }, throwable -> {
                            viewModel.setLoading(false);
                            Log.e(TAG, "getAllShop: " + throwable.getMessage());
                        });
            }
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
                        this.serviceName = listServices.get(0).getName();

                        txtMoreService.setOnClickListener(v -> {
                            ArrayAdapter<String> services = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1);

                            for (int i = 0; i < listServices.size(); i++) {
                                services.add(listServices.get(i).getName());
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Danh sách dịch vụ");
                            builder.setAdapter(services, (dialog, which) -> {
                                clusterShopByService(services.getItem(which));
                                dialog.dismiss();
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });

                    }
                }, throwable -> {
                    viewModel.setLoading(false);
                    Log.e(TAG, "getAllServices: " + throwable.getMessage());
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

        btnTopOneService.setOnClickListener(v -> {
            clusterShopByService("" + btnTopOneService.getText());
        });
        btnTopTwoService.setOnClickListener(v -> {
            clusterShopByService("" + btnTopTwoService.getText());
        });

        // set on list view select ----> set text to the search view
        searchViewListAllShopService.setOnItemClickListener((parent, v, position, id) -> {
            String name = "";
            name = "" + adapter.getItem(position).getName();
            searchViewService.setQuery(name, false);
            searchViewListAllShopService.setVisibility(View.GONE);
        });

    }

    private void observeLoading() {
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
        intent.putExtra("listShop", shops);
        startActivity(intent);
    }

    @Override
    public void onDetailSelected(Shop shop) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("serviceName", "");
        intent.putExtra("shop", shop);
        distance = shop.getDistanceFromUser();
        intent.putExtra("dis", distance);
        intent.putExtra("listShop", shops);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        boolean isOk = false;
        for (int i = 0; i < listAllShopServices.size(); i++) {
            if (listAllShopServices.get(i).getName().equals("" + searchViewService.getQuery())) {
                isOk = true;
                break;
            }
        }
        if (isOk) {
            clusterShopByService("" + searchViewService.getQuery());
        } else {
            SweetAlertDialog errorDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            errorDialog.setTitleText("Thông báo");
            errorDialog.setContentText("Không tìm thấy dịch vụ!");
            errorDialog.setConfirmText("OK");
            errorDialog.setConfirmClickListener(Dialog::dismiss);
            errorDialog.show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText, listAllShopServices);
        if (searchViewListAllShopService.getVisibility() == View.GONE) {
            searchViewListAllShopService.setVisibility(View.VISIBLE);
        }
        return false;
    }

}
