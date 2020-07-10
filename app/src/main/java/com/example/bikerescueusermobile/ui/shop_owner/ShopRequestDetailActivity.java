package com.example.bikerescueusermobile.ui.shop_owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;

import butterknife.BindView;

public class ShopRequestDetailActivity extends BaseActivity {

    @BindView(R.id.shopRequestDetailToolbar)
    Toolbar shopRequestDetailToolbar;

    @BindView(R.id.tvCallBiker)
    TextView tvCallBiker;

    String PhoneNum = "0865137822";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(shopRequestDetailToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Khách hàng");


        tvCallBiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(PhoneNum.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_shop_request_detail;
    }
}
