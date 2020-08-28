package com.example.bikerescueusermobile.ui.shop_owner.shop_history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.util.DateSpliter;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopHistoryFragment extends BaseFragment implements ShopHistorySelectedListener, DatePickerDialog.OnDateSetListener {

    @Override
    protected int layoutRes() {
        return R.layout.shop_history_fragment;
    }

    @BindView(R.id.recycleViewId)
    RecyclerView mRecyclerView;

    @BindView(R.id.pullToRefreshShopReq)
    SwipeRefreshLayout pullToRefreshShopReq;

    @BindView(R.id.edtToDate)
    EditText edtToDate;

    @BindView(R.id.edtFromDate)
    EditText edtFromDate;

    private String TAG = "ShopHistoryFragment";

    @Inject
    ViewModelFactory viewModelFactory;

    private ShopHistoryViewModel viewModel;
    private boolean isFromDateClick = false;
    private Calendar now;
    private DateSpliter dateSpliter;
    private String from;
    private String to;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        now = Calendar.getInstance();
        edtToDate.setHint(now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR));
        edtFromDate.setHint(now.get(Calendar.DAY_OF_MONTH) + "-" + (now.get(Calendar.MONTH)) + "-" + now.get(Calendar.YEAR));

        //set default from date and to date
        dateSpliter = new DateSpliter(edtToDate.getHint().toString());
        to = "" + dateSpliter.getYear() + "-" + dateSpliter.getMonth() + "-" + dateSpliter.getDate();
        dateSpliter = new DateSpliter(edtFromDate.getHint().toString());
        from = "" + dateSpliter.getYear() + "-" + dateSpliter.getMonth() + "-" + dateSpliter.getDate();

        edtFromDate.setOnClickListener(v -> {
            isFromDateClick = true;

            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    ShopHistoryFragment.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
            dpd.setAccentColor(getActivity().getResources().getColor(R.color.core_color));
            dateSpliter = new DateSpliter(edtToDate.getHint().toString());
            Calendar maxDate = Calendar.getInstance();

            maxDate.set(dateSpliter.getYear(), dateSpliter.getMonth() - 1, dateSpliter.getDate());
            dpd.setMaxDate(maxDate);

            dpd.show(getParentFragmentManager(), "Datepickerdialog");
        });

        edtToDate.setOnClickListener(v -> {
            isFromDateClick = false;

            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    ShopHistoryFragment.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            dpd.setVersion(DatePickerDialog.Version.VERSION_1);
            dpd.setAccentColor(getActivity().getResources().getColor(R.color.core_color));
            dpd.setMaxDate(now);

            Calendar minDate = Calendar.getInstance();
            dateSpliter = new DateSpliter(edtFromDate.getHint().toString());

            minDate.set(dateSpliter.getYear(), dateSpliter.getMonth() - 1, dateSpliter.getDate());
            dpd.setMinDate(minDate);

            dpd.show(getParentFragmentManager(), "Datepickerdialog");
        });
        //setup viewmodel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHistoryViewModel.class);
        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));

        getHistory(from, to);
    }

    @Override
    public void onDetailSelected(Request request) {
        Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
        intent.putExtra("reqId", request.getId());
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (isFromDateClick) {
            from = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            edtFromDate.setHint("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            getHistory(from, to);
        } else {
            to = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            edtToDate.setHint("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            getHistory(from, to);
        }
    }

    private void getHistory(String from, String to) {
        viewModel.getRequestByShopId(CurrentUser.getInstance().getId(), from, to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReq -> {
                    if (listReq != null) {
                        if (getActivity() != null) {
                            mRecyclerView.setAdapter(new ShopHistoryRecyclerViewAdapter(listReq, this));
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            pullToRefreshShopReq.setOnRefreshListener(() -> {
                                pullToRefreshShopReq.setRefreshing(false);
                                getHistory(from, to);
                            });
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "getRequestByShopId: " + throwable.getMessage());
                });
    }
}
