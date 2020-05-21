package com.example.bikerescueusermobile.di.component;

import com.example.bikerescueusermobile.base.BaseApplication;
import com.example.bikerescueusermobile.di.module.ActivityBindingModule;
import com.example.bikerescueusermobile.di.module.ApplicationModule;
import com.example.bikerescueusermobile.di.module.ContextModule;
import android.app.Application;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Singleton
@Component(modules = {ContextModule.class,
                      AndroidSupportInjectionModule.class,
                      ApplicationModule.class,
                      ActivityBindingModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(BaseApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        ApplicationComponent build();
    }
}
