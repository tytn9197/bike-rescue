package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.adapter.CustomAdapter;
import com.example.bikerescueusermobile.data.model.request.Request;

import java.util.ArrayList;
import java.util.List;

public class ShopHomeFragment extends BaseFragment implements CustomAdapter.OnRequestListener {


    View v;
    RecyclerView recyclerView;
    List<Request> requestList;
    CustomAdapter customAdapter;

    Toolbar shopHomeToolbar;

    Button toolbarMenu;

    private String TAG = "abc";

    TextView txtMsg;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("mess");
            txtMsg.setText("" + message);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init data
        requestList = new ArrayList<>();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45666", "Phan Gia Cường", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","15 phút trước", R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45667", "Trần Ngọc Tỷ", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","30 phút trước", R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45668", "Ngô Duy Hoàn", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","10 phút trước", R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45669", "Phan Gia Cường", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","20 phút trước", R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45670", "Trần Ngọc Tỷ", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","17 phút trước", R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45671", "Phan Gia Cường", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","25 phút trước",R.drawable.ic_booking, "Đang xử lí"));
//        requestList.add(new Request(R.drawable.ic_user, "Mã đơn hàng: 45672", "Ngô Duy Hoàn", "206/16 đường số 20 phường 5 Gò Vấp","Sữa chữa xe máy","8 phút trước",R.drawable.ic_booking, "Đang xử lí"));
//

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.shop_home_fragment, container, false);
        txtMsg = v.findViewById(R.id.txtMsg);
        recyclerView = v.findViewById(R.id.request_process);

        customAdapter = new CustomAdapter(requestList, getContext(), this);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));

        shopHomeToolbar = v.findViewById(R.id.shopHomeToolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(shopHomeToolbar);
        activity.getSupportActionBar().setTitle("Danh sách yêu cầu mới");

        return v;
    }

    @Override
    protected int layoutRes() {
        return R.layout.shop_home_fragment;
    }

    @Override
    public void onRequestClick(int position) {
        Log.d(TAG, "on request click." + position);
    }
}
