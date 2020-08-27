package com.example.bikerescueusermobile.ui.complain;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;

import butterknife.BindView;

public class ComplainActivity extends BaseActivity {
    @Override
    protected int layoutRes() {
        return R.layout.activity_complain;
    }

    private static final String TAG = "ComplainActivity";

    @BindView(R.id.complainToolbar)
    Toolbar complainToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(complainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
