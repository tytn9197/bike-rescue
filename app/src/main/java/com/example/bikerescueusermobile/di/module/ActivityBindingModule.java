package com.example.bikerescueusermobile.di.module;

import com.example.bikerescueusermobile.ui.loading_page.LoadPageActivity;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.main.MainFragmentBindingModule;
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

}
