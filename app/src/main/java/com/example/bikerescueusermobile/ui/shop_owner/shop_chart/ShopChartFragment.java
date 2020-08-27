package com.example.bikerescueusermobile.ui.shop_owner.shop_chart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.service.CountingService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.favorite.FavoriteRecyclerViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.services.ServiceRecycleViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.services.ServiceViewModel;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopChartFragment extends BaseFragment {
    @Override
    protected int layoutRes() {
        return R.layout.fragment_shop_chart;
    }

    private static final String TAG = "ShopChartFragment";

    @BindView(R.id.rvServiceCounting)
    RecyclerView mRecyclerView;

    @BindView(R.id.txtSuccessReq)
    TextView txtSuccessReq;

    @BindView(R.id.txtAllReq)
    TextView txtAllReq;

    @Inject
    ViewModelFactory viewModelFactory;

    private ServiceViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.requestFocus();

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ServiceViewModel.class);
        viewModel.getAllCountService(CurrentUser.getInstance().getShop().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    int count = 0;

                    if (listServices != null && listServices.size() > 0) {
                        for (CountingService countingService: listServices){
                            count += countingService.getCountRequest();
                        }

                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        mRecyclerView.setAdapter(new ServiceCoutingRecyclerViewAdapter(listServices));
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        Log.e(TAG, "count: " + count);
                    }
                }, throwable -> {
                    Log.e(TAG, "getAllCountService: " + throwable.getMessage());
                });

        viewModel.countAllByAccepted(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(total -> {
                    txtAllReq.setText("" + total);
                }, throwable -> {
                    Log.e(TAG, "countAllByAccepted: " + throwable.getMessage());
                });

        ViewModelProviders.of(this, viewModelFactory).get(ShopUpdateViewModel.class).getSuccessReq(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReq -> {
                    if(listReq != null && listReq.size() > 0){
                        txtSuccessReq.setText("" + listReq.size());
                    }else{
                        txtSuccessReq.setText("0");
                    }
                }, throwable -> {
                    Log.e(TAG, "getSuccessReq: " + throwable.getMessage());
                });
    }
}
