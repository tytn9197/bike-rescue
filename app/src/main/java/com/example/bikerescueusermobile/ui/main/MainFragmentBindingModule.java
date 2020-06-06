package com.example.bikerescueusermobile.ui.main;

import com.example.bikerescueusermobile.ui.favorite.FavoriteShopFragment;
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.home.HomeFragment;
import com.example.bikerescueusermobile.ui.profile.ProfileFragment;
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

    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment provideHistoryFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment provideProfileFragment();

    @ContributesAndroidInjector
    abstract FavoriteShopFragment provideFavoriteShopFragment();
}
