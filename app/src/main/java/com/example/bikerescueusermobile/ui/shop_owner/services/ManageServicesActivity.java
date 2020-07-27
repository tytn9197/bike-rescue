package com.example.bikerescueusermobile.ui.shop_owner.services;

import android.os.Bundle;
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
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ManageServicesActivity extends BaseActivity implements ShopServiceSelectedListener {

    @BindView(R.id.recycleViewServiceId)
    RecyclerView mRecyclerView;

    @BindView(R.id.pullToRefreshShopService)
    SwipeRefreshLayout pullToRefreshShopService;

    @BindView(R.id.shopServiceToolbar)
    Toolbar shopServiceToolbar;

    @BindView(R.id.id)
    TextView id;

    List<ShopServiceTable> serviceList;

    private String TAG = "ShopServiceActivity";

    @Inject
    ViewModelFactory viewModelFactory;

    private ServiceViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopServiceToolbar.setTitle("Danh sách dịch vụ");

        serviceList = new ArrayList<>();
        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ServiceViewModel.class);
        viewModel.getShopServiceByShopOwnerId(CurrentUser.getInstance().getId())
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
                });
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_shop_manage_services;
    }

    @Override
    public void onDetailSelected(ShopServiceTable feed) {

    }
}
