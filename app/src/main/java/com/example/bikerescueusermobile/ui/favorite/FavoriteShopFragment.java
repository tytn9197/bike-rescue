package com.example.bikerescueusermobile.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.login.LoginModel;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteShopFragment extends BaseFragment implements FavoriteSelectedListener {

    private static final String TAG = "FavoriteFragment";

    @BindView(R.id.rvFavorite)
    RecyclerView mRecyclerView;

    @BindView(R.id.my_favorite_loading)
    ProgressBar myFavoriteLoading;

    @BindView(R.id.pullToRefreshFavorite)
    SwipeRefreshLayout pullToRefreshFavorite;

    @BindView(R.id.txtNullFavoriteShop)
    TextView txtNullFavoriteShop;

    @Inject
    ViewModelFactory viewModelFactory;

    private FavoriteViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null)
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(FavoriteViewModel.class);

        viewModel.getFavoriteShopByUserId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listFavoriteShop -> {
                    if (listFavoriteShop != null) {
                        if (listFavoriteShop.size() > 0) {
                            txtNullFavoriteShop.setVisibility(View.GONE);

                            mRecyclerView.setAdapter(new FavoriteRecyclerViewAdapter(listFavoriteShop, this));
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            pullToRefreshFavorite.setOnRefreshListener(() -> pullToRefreshFavorite.setRefreshing(false));
                        } else {
                            txtNullFavoriteShop.setVisibility(View.VISIBLE);
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "getShopByServiceName: " + throwable.getMessage());
                    txtNullFavoriteShop.setVisibility(View.VISIBLE);
                });

    }

    @Override
    protected int layoutRes() {
        return R.layout.favorite_fragment;
    }

    @Override
    public void onDetailSelected(Shop shop) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("serviceName", "");
        intent.putExtra("shop", shop);
        double distance = MyMethods.getDistanceBetweenShopAndUser(
                CurrentUser.getInstance().getLatitude(),
                CurrentUser.getInstance().getLongtitude(),
                shop.getLatitude(),
                shop.getLongtitude());
        shop.setDistanceFromUser(distance);
        intent.putExtra("dis", distance);
        startActivity(intent);
    }
}
