package com.example.bikerescueusermobile.di.module;

import com.example.bikerescueusermobile.data.model.user.IUserService;
import com.example.bikerescueusermobile.data.rest.RepoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class ApplicationModule {
    private static final String BASE_URL = "https://88c7072ec39e.jp.ngrok.io/";

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

}
