package com.example.bikerescueusermobile.ui.favorite;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikerescueusermobile.data.model.favorite.Favorite;
import com.example.bikerescueusermobile.data.model.favorite.FavoriteRepository;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository favoriteRepository;

    @Inject
    public FavoriteViewModel(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Single<List<Shop>> getFavoriteShopByUserId(int id){
        return favoriteRepository.getFavoriteShopByUserId(id);
    }
}
