package com.example.bikerescueusermobile.di.module;

import com.example.bikerescueusermobile.data.dont_use_it.RepoService;
import com.example.bikerescueusermobile.data.model.configuration.IConfigurationService;
import com.example.bikerescueusermobile.data.model.request.IRequestService;
import com.example.bikerescueusermobile.data.model.shop.IShopService;
import com.example.bikerescueusermobile.data.model.user.IUserService;
import com.example.bikerescueusermobile.data.model.shop_services.IShopServicesService;
import com.example.bikerescueusermobile.data.model.vehicle.IVehicleService;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class ApplicationModule {
    private static final String BASE_URL = "https://4b9e4bf8caae.jp.ngrok.io";

    @Singleton
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static RepoService provideRetrofitService(Retrofit retrofit){
        return retrofit.create(RepoService.class);
    }

    @Singleton
    @Provides
    static IUserService provideUserService(Retrofit retrofit){
        return retrofit.create(IUserService.class);
    }

    @Singleton
    @Provides
    static IShopServicesService provideShopServicesService(Retrofit retrofit){
        return retrofit.create(IShopServicesService.class);
    }

    @Singleton
    @Provides
    static IShopService provideShopService(Retrofit retrofit){
        return retrofit.create(IShopService.class);
    }

    @Singleton
    @Provides
    static IConfigurationService provideConfigurationService(Retrofit retrofit){
        return retrofit.create(IConfigurationService.class);
    }

    @Singleton
    @Provides
    static IRequestService provideRequestService(Retrofit retrofit){
        return retrofit.create(IRequestService.class);
    }

    @Singleton
    @Provides
    static IVehicleService provideIVehicleService(Retrofit retrofit){
        return retrofit.create(IVehicleService.class);
    }
}
