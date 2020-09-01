package com.example.bikerescueusermobile.ui.history;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.util.DateSpliter;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends BaseFragment implements HistorySelectedListener,
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    @Override
    protected int layoutRes() {
        return R.layout.biker_history_fragment;
    }

    private static final String TAG = "HistoryFragment";

    @BindView(R.id.rvHistory)
    RecyclerView mRecyclerView;

    @BindView(R.id.my_feed_loading)
    ProgressBar myFeedLoading;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.edtToDate)
    EditText edtToDate;

    @BindView(R.id.edtFromDate)
    EditText edtFromDate;

    @BindView(R.id.spinStatus)
    Spinner spinStatus;

    @BindView(R.id.txtListNull)
    TextView txtListNull;

    @Inject
    ViewModelFactory viewModelFactory;

    private HistoryViewModel viewModel;
    private List<Request> listReq;
    private boolean isFromDateClick = false;
    private Calendar now;
    private DateSpliter dateSpliter;
    private String from;
    private String to;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("BikeRescueBiker"));

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel.class);
        listReq = new ArrayList<>();

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
                    HistoryFragment.this,
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
                    HistoryFragment.this,
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));

        ArrayAdapter<String> statuses = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        statuses.add("Đang xử lí");
        statuses.add("Hoàn thành");
        statuses.add("Từ chối");
        statuses.add("Đã hủy");
        spinStatus.setAdapter(statuses);
        spinStatus.setPrompt("Đang xử lí");
        spinStatus.setOnItemSelectedListener(this);

        getHistory(from, to, 0);
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
            getHistory(from, to, spinStatus.getSelectedItemPosition());
        } else {
            to = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            edtToDate.setHint("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            getHistory(from, to, spinStatus.getSelectedItemPosition());
        }
    }

    private void getHistory(String from, String to, int statusPos){
        String status = "";
        switch (statusPos){
            case 0 :
                status = "PROCESSING";
                break;
            case 1 :
                status = MyInstances.STATUS_FINISHED;
                break;
            case 2 :
                status = MyInstances.STATUS_REJECTED;
                break;
            case 3 :
                status = MyInstances.STATUS_CANCELED;
                break;
        }

        viewModel.getRequestByBikerId(CurrentUser.getInstance().getId(), from, to, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listReq -> {
                    this.listReq.addAll(listReq);
                    if (getActivity() != null) {
                        txtListNull.setVisibility(View.GONE);
                        mRecyclerView.setAdapter(new HistoryRecyclerViewAdapter(listReq, this));
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        pullToRefresh.setOnRefreshListener(() -> {
                            pullToRefresh.setRefreshing(false);
                            getHistory(from, to, statusPos);
                        });
                        if(listReq.size() <= 0){
                            txtListNull.setVisibility(View.VISIBLE);
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "getRequestByBikerId: " + throwable.getMessage());
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getHistory(from, to, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String message = intent.getStringExtra("message");
                Gson gson = new Gson();

                MessageRequestFB responeReq = gson.fromJson(message, MessageRequestFB.class);
                if(responeReq != null){
                    getHistory(from, to, spinStatus.getSelectedItemPosition());
                }
            }
        }
    };
}
