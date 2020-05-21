package com.example.bikerescueusermobile.ui.main;

import com.example.bikerescueusermobile.ui.detail.DetailsFragment;
import com.example.bikerescueusermobile.ui.list.ListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract ListFragment provideListFragment();

    @ContributesAndroidInjector
    abstract DetailsFragment provideDetailsFragment();
}
