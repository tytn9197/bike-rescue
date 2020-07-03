package com.example.bikerescueusermobile.ui.map;

import com.example.bikerescueusermobile.ui.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapFragmentBindingModule {
    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();
}
