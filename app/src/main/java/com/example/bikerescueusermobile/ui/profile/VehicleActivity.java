package com.example.bikerescueusermobile.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;
import com.example.bikerescueusermobile.data.model.vehicle.VehicleDTO;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.confirm.ConfirmViewModel;
import com.example.bikerescueusermobile.ui.favorite.FavoriteRecyclerViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VehicleActivity extends BaseActivity implements VehicleSelectedListener {

    @Override
    protected int layoutRes() {
        return R.layout.activity_vehicle;
    }

    private static final String TAG = "VehicleActivity";

    @BindView(R.id.vehicleToolbar)
    Toolbar vehicleToolbar;

    @BindView(R.id.rvVehicle)
    RecyclerView rvVehicle;

    @BindView(R.id.pullToRefreshVehicle)
    SwipeRefreshLayout pullToRefreshVehicle;

    @BindView(R.id.updateContainer)
    LinearLayout updateContainer;

    @BindView(R.id.edtBrand)
    EditText edtBrand;

    @BindView(R.id.listYear)
    Spinner listYear;

    @BindView(R.id.listType)
    Spinner listType;

    @BindView(R.id.btnUpdateVehicle)
    Button btnUpdateVehicle;

    @BindView(R.id.btnCreateVehicle)
    Button btnCreateVehicle;

    @BindView(R.id.vehicleStatus)
    Switch vehicleStatus;

    @BindView(R.id.addVehicle)
    ImageView addVehicle;

    @Inject
    ViewModelFactory viewModelFactory;

    private ConfirmViewModel viewModel;

    private ArrayAdapter<String> years;
    private ArrayAdapter<String> types;
    private int vehicleId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(vehicleToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin xe của bạn");

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmViewModel.class);

        //get all user's vehicle
        viewModel.getVehicleByUserId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listVehicles -> {
                    if(listVehicles.size() > 0){
                        updateContainer.setVisibility(View.GONE);
                    }

                    rvVehicle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    rvVehicle.setAdapter(new VehicleRecyclerViewAdapter(listVehicles, this));
                    rvVehicle.setLayoutManager(new LinearLayoutManager(this));
                    pullToRefreshVehicle.setOnRefreshListener(() -> pullToRefreshVehicle.setRefreshing(false));
                }, throwable -> {
                    Log.e(TAG, "getVehicleByUserId: " + throwable.getMessage());
                });

        //get all vehicle title
        viewModel.getAllConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listConfig -> {
                    if (listConfig != null) {
//                        ArrayAdapter<String> brands = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
                        years = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
                        types = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

                        for (int i = 0; i < listConfig.size(); i++) {
//                            if (listConfig.get(i).getName().equals("brand name")){
//                                brands.add(listConfig.get(i).getValue());
//                            }
                            if (listConfig.get(i).getName().equals("vehicle year")) {
                                years.add(listConfig.get(i).getValue());
                            }
                            if (listConfig.get(i).getName().equals("vehicle type")) {
                                types.add(listConfig.get(i).getValue());
                            }
                        }

//                        listBrand.setAdapter(brands);
                        listType.setAdapter(types);
                        listYear.setAdapter(years);
                    }
                }, throwable -> {
                    Log.e(TAG, "getAllConfig: " + throwable.getMessage());
                });

        rvVehicle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //Scrolled Downwards
                    updateContainer.setVisibility(View.GONE);
                }
            }
        });

        addVehicle.setOnClickListener(v -> {
            updateContainer.setVisibility(View.VISIBLE);
            btnUpdateVehicle.setVisibility(View.GONE);
            btnCreateVehicle.setVisibility(View.VISIBLE);

            edtBrand.setText("");
            listYear.setSelection(0);
            listType.setSelection(0);
            vehicleStatus.setChecked(false);

        });

        btnCreateVehicle.setOnClickListener(v->{
            VehicleDTO vehicle = new VehicleDTO();

            vehicle.setType(listType.getSelectedItem().toString());
            vehicle.setBrand(edtBrand.getText().toString());
            if(vehicleStatus.isChecked()){
                vehicle.setStatus(true);
            }else{
                vehicle.setStatus(false);
            }
            vehicle.setUserId(CurrentUser.getInstance().getId());
            vehicle.setVehiclesYear(Integer.parseInt(listYear.getSelectedItem().toString()));
            Log.e(TAG, "createVehicle VehicleDTO vehicle: " + vehicle.toString());

            viewModel.createVehicle(vehicle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(respone -> {
                        Log.e(TAG, "createVehicle respone: " + respone.toString());
                        finish();
                        startActivity(getIntent());
                    }, throwable -> {
                        Log.e(TAG, "createVehicle: " + throwable.getMessage());
                    });
        });

        btnUpdateVehicle.setOnClickListener(v -> {
            updateContainer.setVisibility(View.GONE);

            VehicleDTO vehicle = new VehicleDTO();

            vehicle.setId(vehicleId);
            vehicle.setType(listType.getSelectedItem().toString());
            vehicle.setBrand(edtBrand.getText().toString());
            if(vehicleStatus.isChecked()){
                vehicle.setStatus(true);
            }else{
                vehicle.setStatus(false);
            }
            vehicle.setUserId(CurrentUser.getInstance().getId());
            vehicle.setVehiclesYear(Integer.parseInt(listYear.getSelectedItem().toString()));
            Log.e(TAG, "updateVehicle VehicleDTO vehicle: " + vehicle.toString());

            viewModel.updateVehicle(vehicle, vehicleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(respone -> {
                        Log.e(TAG, "updateVehicle respone: " + respone.toString());
                        finish();
                        startActivity(getIntent());
                    }, throwable -> {
                        Log.e(TAG, "updateVehicle: " + throwable.getMessage());
                    });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetailSelected(Vehicle vehicle) {
        updateContainer.setVisibility(View.VISIBLE);
        btnUpdateVehicle.setVisibility(View.VISIBLE);
        btnCreateVehicle.setVisibility(View.GONE);

        edtBrand.setText(vehicle.getBrand());
        vehicleId = vehicle.getId();

        for (int i = 0; i < years.getCount(); i++) {
            if (years.getItem(i).equals("" + vehicle.getVehiclesYear())) {
                listYear.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < types.getCount(); i++) {
            if (types.getItem(i).equals("" + vehicle.getType())) {
                listType.setSelection(i);
                break;
            }
        }

        if (vehicle.isStatus()) {
            vehicleStatus.setChecked(true);
        } else {
            vehicleStatus.setChecked(false);
        }
    }

    @Override
    public void onLongClick(Vehicle vehicle) {
        Log.e("aa", "test long click ok");
    }
}
