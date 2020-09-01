package com.example.bikerescueusermobile.ui.shop_owner.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.data.model.service.Service;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceDTO;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManageServicesActivity extends BaseActivity implements ShopServiceSelectedListener {

    @BindView(R.id.recycleViewServiceId)
    RecyclerView mRecyclerView;

    @BindView(R.id.pullToRefreshShopService)
    SwipeRefreshLayout pullToRefreshShopService;

    @BindView(R.id.shopServiceToolbar)
    Toolbar shopServiceToolbar;

    @BindView(R.id.addService)
    ImageView addService;

    List<ShopServiceTable> serviceList;

    private String TAG = "ShopServiceActivity";

    @Inject
    ViewModelFactory viewModelFactory;

    private ServiceViewModel viewModel;
    private List<Service> listSystemService;
    private ArrayAdapter<String> serviceNames;
    private int listSerPosition;
    private CheckBox cbLienHe;
    private EditText edtSerPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopServiceToolbar.setTitle("Danh sách dịch vụ");

        serviceNames = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        serviceList = new ArrayList<>();
        listSystemService = new ArrayList<>();
        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ServiceViewModel.class);

        viewModel.getAllShopServiceByShopId(CurrentUser.getInstance().getShop().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    if (listServices != null && listServices.size() > 0) {
                        serviceList.addAll(listServices);
                        ServiceRecycleViewAdapter adapter = new ServiceRecycleViewAdapter(listServices, this);
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        pullToRefreshShopService.setOnRefreshListener(() -> {
                            pullToRefreshShopService.setRefreshing(false);
                            finish();
                            startActivity(getIntent());
                        });
                    }
                }, throwable -> {
                    Log.e(TAG, "getShopServiceByShopOwnerId: " + throwable.getMessage());
                });

        viewModel.getAllService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    if (listServices != null && listServices.size() > 0) {
                        listSystemService.addAll(listServices);
                    }
                }, throwable -> {
                    Log.e(TAG, "getAllService: " + throwable.getMessage());
                });

        addService.setOnClickListener(v -> {
            setupServiceDialog(false, null);
        });
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_shop_manage_services;
    }

    @Override
    public void onDetailSelected(ShopServiceTable service) {
        setupServiceDialog(true, service);
    }

    @SuppressLint("SetTextI18n")
    private void setupServiceDialog(boolean isUpdate, ShopServiceTable service) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View serviceView = factory.inflate(R.layout.dialog_service_detail, null);
        AlertDialog serviceDialog = new AlertDialog.Builder(this).create();

        Button btnConfirm = serviceView.findViewById(R.id.btn_confirm);
        Spinner listService = serviceView.findViewById(R.id.listService);
        edtSerPrice = serviceView.findViewById(R.id.edtSerPrice);
        Switch status = serviceView.findViewById(R.id.serviceStatus);
        cbLienHe = serviceView.findViewById(R.id.cbLienHe);

        cbLienHe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lienHeMode();
            } else {
                priceMode();
            }
        });

        listService.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item));
        if (listSystemService.size() > 0) {
            for (int i = 0; i < listSystemService.size(); i++) {
                serviceNames.add(listSystemService.get(i).getServiceName());
                if (service != null)
                    if (service.getServices().getName().equals(listSystemService.get(i).getServiceName())) {
                        listSerPosition = i;
                    }
            }
            listService.setAdapter(serviceNames);
        }

        if (isUpdate) {
            btnConfirm.setText("Cập nhập");
            if (service.getPrice() >= 0) {
                edtSerPrice.setText("" + service.getPrice().intValue());
                priceMode();
            } else {
                lienHeMode();
            }
            status.setChecked(service.isStatus());
            listService.setAdapter(serviceNames);
            listService.setEnabled(false);
            listService.setClickable(false);
            cbLienHe.setEnabled(false);
            cbLienHe.setClickable(false);

        } else {
            btnConfirm.setText("Thêm");
        }

        serviceDialog.setView(serviceView);
        serviceDialog.setCanceledOnTouchOutside(false);
        serviceDialog.setCancelable(false);

        serviceView.findViewById(R.id.btn_return).setOnClickListener(v1 -> serviceDialog.dismiss());

        btnConfirm.setOnClickListener(confirmView -> {
            serviceDialog.dismiss();
            if (isUpdate) {
                ShopServiceTable updateService = new ShopServiceTable();
                //newService.setServices(service.getServices());
                //newService.setShops(CurrentUser.getInstance().getShop());
                if(service.getPrice() >= 0){
                    updateService.setPrice(Double.parseDouble(edtSerPrice.getText().toString()));
                } else {
                    updateService.setPrice(Double.parseDouble("-1"));
                }
                //status
                if (status.isChecked()) {
                    updateService.setStatus(true);
                } else {
                    updateService.setStatus(false);
                }

                viewModel.updateShopService(service.getId(), updateService)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response != null) {
                                finish();
                                startActivity(getIntent());
                            }
                        }, throwable -> {
                            Log.e(TAG, "Update Service: " + throwable.getMessage());
                        });
            } else {
                ShopServiceDTO newService = new ShopServiceDTO();
                //set shop Id
                newService.setShopId(CurrentUser.getInstance().getShop().getId());

                //set service Id
                int position = listService.getSelectedItemPosition();
                newService.setServiceId(listSystemService.get(position).getId());
                //set price

                if(cbLienHe.isChecked()){
                    newService.setPrice(Double.parseDouble("-1"));
                } else {
                    newService.setPrice(Double.parseDouble(edtSerPrice.getText().toString()));
                }
                //status
                if (status.isChecked()) {
                    newService.setStatus(true);
                } else {
                    newService.setStatus(false);
                }
                //set unit

                viewModel.createShopService(newService)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response != null) {
                                finish();
                                startActivity(getIntent());
                            }
                        }, throwable -> {
                            Log.e(TAG, "Add new service: " + throwable.getMessage());
                        });
            }
        });
        serviceDialog.show();
    }

    private void lienHeMode() {
        cbLienHe.setChecked(true);
        edtSerPrice.setVisibility(View.GONE);
    }

    private void priceMode() {
        cbLienHe.setChecked(false);
        edtSerPrice.setVisibility(View.VISIBLE);
    }


}
