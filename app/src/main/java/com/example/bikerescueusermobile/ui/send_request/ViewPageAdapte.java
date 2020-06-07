package com.example.bikerescueusermobile.ui.send_request;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapte extends FragmentPagerAdapter {

    private final List<Fragment> fragmentsList = new ArrayList<>();
    private final List<String> fragmentListTitle = new ArrayList<>();
    public ViewPageAdapte(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);

    }

    @Override
    public int getCount() {
        return fragmentListTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitle.get(position);
    }
    public void AddFragment (Fragment fragment, String title){
        fragmentsList.add(fragment);
        fragmentListTitle.add(title);
    }
}
