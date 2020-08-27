package com.example.bikerescueusermobile.ui.shopMain;




import com.example.bikerescueusermobile.ui.shop_owner.shop_chart.ShopChartFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_history.ShopHistoryFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_home.ShopHomeFragment;
import com.example.bikerescueusermobile.ui.shop_owner.shop_profile.ShopProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ShopMainFragmentBindingModule {
    @ContributesAndroidInjector
    abstract ShopHomeFragment provideShopHomeFragment();

    @ContributesAndroidInjector
    abstract ShopHistoryFragment provideShopHistoryFragment();

    @ContributesAndroidInjector
    abstract ShopProfileFragment provideShopProfileFragment();

    @ContributesAndroidInjector
    abstract ShopChartFragment provideShopChartFragment();

}
