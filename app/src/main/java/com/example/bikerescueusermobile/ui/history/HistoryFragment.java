package com.example.bikerescueusermobile.ui.history;

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
import com.example.bikerescueusermobile.data.model.request.BikerRequest;
import com.example.bikerescueusermobile.ui.loading_page.LoadPageActivity;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HistoryFragment extends BaseFragment implements HistorySelectedListener{
    @Override
    protected int layoutRes() {
        return R.layout.biker_history_fragment;
    }

    @BindView(R.id.rvHistory)
    RecyclerView mRecyclerView;

    @BindView(R.id.my_feed_loading)
    ProgressBar myFeedLoading;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BikerRequest b1 = new BikerRequest(1,"Xe chết máy", "30/05/2020 lúc 13:00", 5);
        BikerRequest b2 = new BikerRequest(2,"Xe hết bình", "30/06/2020 lúc 11:00",2);
        BikerRequest b3 = new BikerRequest(3,"Xe thủng lốp", "21/04/2020 lúc 09:00",3);
        BikerRequest b4 = new BikerRequest(4,"Xe chết máy", "06/05/2020 lúc 18:00",2);
        BikerRequest b5 = new BikerRequest(5,"Xe chết máy", "19/05/2020 lúc 01:00",5);

        List<BikerRequest> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);

        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(list, this,this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pullToRefresh.setOnRefreshListener(() -> pullToRefresh.setRefreshing(false));
    }

    @Override
    public void onDetailSelected(BikerRequest feed) {
//        Intent intent = new Intent(HistoryFragment.this.getActivity(), LoadPageActivity.class);
//        intent.putExtra("caseCode", feed.getCaseCode());
//        startActivity(intent);
        Toast.makeText(getActivity(), "feed id: " + feed.getId() + "---feed name: " + feed.getName(), Toast.LENGTH_SHORT).show();
    }
}
