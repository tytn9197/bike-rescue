package com.example.bikerescueusermobile.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FavoriteShopFragment extends BaseFragment implements FavoriteSelectedListener {

    @BindView(R.id.rvFavorite)
    RecyclerView mRecyclerView;

    @BindView(R.id.my_favorite_loading)
    ProgressBar myFavoriteLoading;

    @BindView(R.id.pullToRefreshFavorite)
    SwipeRefreshLayout pullToRefreshFavorite;

    @Inject
    ViewModelFactory viewModelFactory;

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


        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new FavoriteRecyclerViewAdapter(list, this, this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefreshFavorite.setOnRefreshListener(() -> pullToRefreshFavorite.setRefreshing(false));
    }

    @Override
    protected int layoutRes() {
        return R.layout.favorite_fragment;
    }

    @Override
    public void onDetailSelected(Shop shop) {
//        Intent intent = new Intent(getActivity(), LoadPageActivity.class);
//        intent.putExtra("caseCode", feed.getCaseCode());
//        startActivity(intent);
        Toast.makeText(getActivity(), "shop id: " + shop.getId() + " --- shop name: " + shop.getShopName(), Toast.LENGTH_SHORT).show();
    }
}
