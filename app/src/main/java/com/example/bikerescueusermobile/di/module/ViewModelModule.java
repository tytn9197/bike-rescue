package com.example.bikerescueusermobile.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bikerescueusermobile.di.util.ViewModelKey;
import com.example.bikerescueusermobile.ui.detail.DetailsViewModel;
import com.example.bikerescueusermobile.ui.list.ListViewModel;
import com.example.bikerescueusermobile.ui.login.LoginModel;
import com.example.bikerescueusermobile.ui.register.RegisterViewModel;
import com.example.bikerescueusermobile.ui.seach_shop_service.ShopServiceViewModel;
import com.example.bikerescueusermobile.util.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel.class)
    abstract ViewModel bindListViewModel(ListViewModel listViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    abstract ViewModel bindDetailsViewModel(DetailsViewModel detailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginModel.class)
    abstract ViewModel bindLoginViewModel(LoginModel loginModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShopServiceViewModel.class)
    abstract ViewModel bindShopServiceViewModel(ShopServiceViewModel shopServiceViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);

}
