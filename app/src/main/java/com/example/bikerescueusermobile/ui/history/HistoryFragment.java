package com.example.bikerescueusermobile.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends BaseFragment implements HistorySelectedListener {
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

    private HistoryViewModel viewModel;
    private List<Request> listReq;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel.class);
        listReq = new ArrayList<>();

        viewModel.getRequestByBikerId(CurrentUser.getInstance().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReq -> {
                    this.listReq.addAll(listReq);
                    if (getActivity() != null) {
                        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));
                        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(listReq, this));
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        pullToRefresh.setOnRefreshListener(() -> {
                            pullToRefresh.setRefreshing(false);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_container, new HistoryFragment())
                                    .commit();
                        });
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
