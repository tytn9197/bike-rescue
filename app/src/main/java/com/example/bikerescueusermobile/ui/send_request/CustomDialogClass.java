package com.example.bikerescueusermobile.ui.send_request;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bikerescueusermobile.R;
import com.google.android.material.tabs.TabLayout;

public class CustomDialogClass  extends DialogFragment implements View.OnClickListener,SelectTab {

    public AppCompatActivity c;
    public Dialog d;
    public Button yes, no;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton closeBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.custom_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // lấy giá trị tự bundle
        tabLayout = view.findViewById(R.id.tabLayout2);
        viewPager =  view.findViewById(R.id.idViewPager2);
        closeBtn = view.findViewById(R.id.close_btn);
        ViewPageAdapte viewPageAdapte = new ViewPageAdapte(getChildFragmentManager());
        //add fargmnet
        viewPageAdapte.AddFragment(new FragmentStep1(this),"Bước 1");
        viewPageAdapte.AddFragment(new FragmentStep2(this),"Bước 2");
        viewPageAdapte.AddFragment(new FragmentStep3(),"Bước 3");
        viewPager.setAdapter(viewPageAdapte);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroyView();
            }
        });

    }
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_yes:
//                c.finish();
//                break;
//            case R.id.btn_no:
//                dismiss();
//                break;
//            default:
//                break;
//        }
        dismiss();
    }

    @Override
    public void selectTab(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > tabLayout.getTabCount()) throw new IndexOutOfBoundsException("The tab number out of bound");
        tabLayout.getTabAt(index).select();
    }
}