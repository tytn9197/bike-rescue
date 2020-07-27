package com.example.bikerescueusermobile.ui.shop_owner.shop_history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopHistoryFragment extends BaseFragment implements ShopHistorySelectedListener {

    @Override
    protected int layoutRes() {
        return R.layout.shop_history_fragment;
    }

    @BindView(R.id.recycleViewId)
    RecyclerView mRecyclerView;

    List<Request> requestList;
    List<Request> listHistory;


    @BindView(R.id.pullToRefreshShopReq)
    SwipeRefreshLayout pullToRefreshShopReq;


    @BindView(R.id.shopHistoryToolbar)
    Toolbar shopHistoryToolbar;

    private String TAG = "ShopHistoryFragment";

    @Inject
    ViewModelFactory viewModelFactory;

    private ShopHistoryViewModel viewModel;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(shopHistoryToolbar);
        activity.getSupportActionBar().setTitle("Danh sách yêu cầu đã thực hiện");

        requestList = new ArrayList<>();
        listHistory = new ArrayList<>();

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHistoryViewModel.class);
        viewModel.getRequestByShopId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReq -> {
                    if (listReq != null && listReq.size() > 0) {
                        //Define
                        requestList.addAll(listReq);
                        listHistory.addAll(listReq);


                        if (getActivity() != null) {
                            mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
                            mRecyclerView.setAdapter(new ShopHistoryRecyclerViewAdapter(listReq, this));
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            pullToRefreshShopReq.setOnRefreshListener(() -> {
                                pullToRefreshShopReq.setRefreshing(false);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_container, new ShopHistoryFragment())
                                        .commit();
                            });
                        }
                    }
                });

    }

    @Override
    public void onDetailSelected(Request request) {
        Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
        intent.putExtra("reqId", request.getId());
        startActivity(intent);
    }

}
