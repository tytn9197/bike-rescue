package com.example.bikerescueusermobile.data.model.configuration;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.http.Path;

public class ConfigurationRepository {
    private final IConfigurationService configurationService;

    @Inject
    public ConfigurationRepository(IConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public Single<List<MyConfiguaration>> getConfiguarationByName(String configName,String token){
        return configurationService.getConfiguarationByName(configName,token);
    }

    public Single<List<MyConfiguaration>> getAllConfig(String token){
        return configurationService.getAllConfig(token);
    }
}
