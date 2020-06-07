package com.example.bikerescueusermobile.ui.send_request;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.bikerescueusermobile.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class SendRequestActivity extends AppCompatActivity {

    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    Button supportBtn;
    CustomDialogClass userInfoDialog;
    FragemtMota fragemtMota;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_page);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        viewPager = (ViewPager) findViewById(R.id.idViewPager);
        supportBtn = (Button) findViewById(R.id.buttonHotro);
        ViewPageAdapte viewPageAdapte = new ViewPageAdapte(getSupportFragmentManager());
        //add fargmnet
        viewPageAdapte.AddFragment(new FragemtMota(),"Mô Tả");
        viewPageAdapte.AddFragment(new FragmentBanggia(),"Bảng Giá");

        viewPager.setAdapter(viewPageAdapte);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoDialog != null
                        && userInfoDialog.getDialog() != null
                        && userInfoDialog.getDialog().isShowing()
                        && !userInfoDialog.isRemoving()) {
                    //dialog is showing so do something
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    userInfoDialog = new CustomDialogClass();
                    userInfoDialog.setCancelable(false);
                    userInfoDialog.show(fm, null);
                }
            }
        });


    }
}
