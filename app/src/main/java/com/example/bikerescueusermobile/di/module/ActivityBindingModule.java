package com.example.bikerescueusermobile.di.module;

import android.app.Activity;

import com.example.bikerescueusermobile.di.util.ViewModelKey;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.main.MainFragmentBindingModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ActivityBindingModule {


    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

}
