package com.example.bikerescueusermobile.ui.shop_owner.shop_chart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.services.ServiceRecycleViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.services.ServiceViewModel;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryViewModel;
import com.example.bikerescueusermobile.util.DateSpliter;
import com.example.bikerescueusermobile.util.ViewModelFactory;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopChartFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener{
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

    @BindView(R.id.edtToDate)
    EditText edtToDate;

    @BindView(R.id.edtFromDate)
    EditText edtFromDate;

    @Inject
    ViewModelFactory viewModelFactory;

    private ServiceViewModel viewModel;
    private boolean isFromDateClick = false;
    private Calendar now;
    private DateSpliter dateSpliter;
    private String from;
    private String to;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.requestFocus();

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
                    ShopChartFragment.this,
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
                    ShopChartFragment.this,
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ServiceViewModel.class);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        getHistory(from, to);
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
        viewModel.getAllCountService(CurrentUser.getInstance().getShop().getId(), from, to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listServices -> {
                    int count = 0;

                    if (listServices != null) {
                        for (CountingService countingService: listServices){
                            count += countingService.getCountRequest();
                        }

                        mRecyclerView.setAdapter(new ServiceCoutingRecyclerViewAdapter(listServices));
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        Log.e(TAG, "count: " + count);
                    }
                }, throwable -> {
                    Log.e(TAG, "getAllCountService: " + throwable.getMessage());
                });

        viewModel.countAllByAccepted(CurrentUser.getInstance().getId(), from, to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(total -> {
                    txtAllReq.setText("" + total);
                }, throwable -> {
                    Log.e(TAG, "countAllByAccepted: " + throwable.getMessage());
                });

        ViewModelProviders.of(this, viewModelFactory).get(ShopUpdateViewModel.class)
                .getSuccessReq(CurrentUser.getInstance().getId(), from, to)
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
