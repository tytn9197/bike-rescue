package com.example.bikerescueusermobile.ui.shop_owner.shop_history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.adapter.CustomAdapter;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopHistoryFragment extends BaseFragment implements CustomAdapter.OnRequestListener {
    @BindView(R.id.recycleViewId)
    RecyclerView recyclerView;


    List<Request> requestList;
    CustomAdapter customAdapter;

    @BindView(R.id.shopHistoryToolbar)
    Toolbar shopHistoryToolbar;
    private String TAG = "abc";

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

        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHistoryViewModel.class);
        viewModel.getRequestByShopId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requests -> {
                    Log.e(TAG, "Request: " + requests);
                    if (requests != null && requests.size() > 0) {
                        //Define
                        requestList.addAll(requests);

                        customAdapter = new CustomAdapter(requests, getContext(), this);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
                    }
                });

    }


    @Override
    protected int layoutRes() {
        return R.layout.shop_history_fragment;
    }

    @Override
    public void onRequestClick(int position) {
        Log.d(TAG, "on request click." + position);
    }
}
