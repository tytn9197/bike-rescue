package com.example.bikerescueusermobile.ui.profile;

import com.example.bikerescueusermobile.ui.detail.DetailsFragment;
import com.example.bikerescueusermobile.ui.favorite.FavoriteShopFragment;
import com.example.bikerescueusermobile.ui.history.HistoryFragment;
import com.example.bikerescueusermobile.ui.home.HomeFragment;
import com.example.bikerescueusermobile.ui.list.ListFragment;
import com.example.bikerescueusermobile.ui.seach_shop_service.SearchShopServiceFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class VehicleFragmentBindingModule {

    @ContributesAndroidInjector
    abstract UpdateVehicleFragment updateVehicleFragment();
}
