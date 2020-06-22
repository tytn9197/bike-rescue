package com.example.bikerescueusermobile.di.module;

import com.example.bikerescueusermobile.ui.confirm.ConfirmInfoActivity;
import com.example.bikerescueusermobile.ui.loading_page.LoadPageActivity;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.main.MainFragmentBindingModule;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;

import com.example.bikerescueusermobile.ui.map.MapActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {


    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract LoadPageActivity bindLoadPageActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract MapActivity bindMapActivity();

    @ContributesAndroidInjector
    abstract CreatePasswordActivity bindcreatePasswordActivity();

    @ContributesAndroidInjector
    abstract ConfirmInfoActivity confirmInfoActivity();

    @ContributesAndroidInjector
    abstract UpdateInfoActivity updateInfoActivity();

}
