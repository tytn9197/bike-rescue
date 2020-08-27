package com.example.bikerescueusermobile.di.module;

import com.example.bikerescueusermobile.ui.complain.ComplainActivity;
import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.create_request.RequestDetailActivity;
import com.example.bikerescueusermobile.ui.loading_page.LoadPageActivity;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.main.MainFragmentBindingModule;
import com.example.bikerescueusermobile.ui.map.MapFragmentBindingModule;
import com.example.bikerescueusermobile.ui.profile.BikerUpdateInfoActivity;
import com.example.bikerescueusermobile.ui.profile.VehicleActivity;
import com.example.bikerescueusermobile.ui.profile.VehicleFragmentBindingModule;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainActivity;
import com.example.bikerescueusermobile.ui.shopMain.ShopMainFragmentBindingModule;
import com.example.bikerescueusermobile.ui.shop_owner.ShopRequestDetailActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.ui.shop_owner.services.ManageServicesActivity;
import com.example.bikerescueusermobile.ui.tracking_map.TrackingMapActivity;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {ShopMainFragmentBindingModule.class})
    abstract ShopMainActivity bindShopMainActivity();

    @ContributesAndroidInjector
    abstract LoadPageActivity bindLoadPageActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract CreatePasswordActivity bindcreatePasswordActivity();

    @ContributesAndroidInjector
    abstract ConfirmInfoActivity confirmInfoActivity();

    @ContributesAndroidInjector
    abstract UpdateInfoActivity updateInfoActivity();

    @ContributesAndroidInjector
    abstract RequestDetailActivity requestDetailActivity();

    @ContributesAndroidInjector(modules = {MapFragmentBindingModule.class})
    abstract MapActivity bindMapActivity();

    @ContributesAndroidInjector
    abstract ShopUpdateInfoActivity shopUpdateInfoActivity();

    @ContributesAndroidInjector
    abstract ShopRequestDetailActivity shopRequestDetailActivity();

    @ContributesAndroidInjector
    abstract BikerUpdateInfoActivity bikerUpdateInfoActivity();

    @ContributesAndroidInjector
    abstract ManageServicesActivity manageServicesActivity();

    @ContributesAndroidInjector(modules = {VehicleFragmentBindingModule.class})
    abstract VehicleActivity vehicleActivity();

    @ContributesAndroidInjector
    abstract TrackingMapActivity trackingMapActivity();

    @ContributesAndroidInjector
    abstract ComplainActivity complainActivity();

}
