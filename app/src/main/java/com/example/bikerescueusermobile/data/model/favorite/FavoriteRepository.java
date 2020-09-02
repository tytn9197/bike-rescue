package com.example.bikerescueusermobile.data.model.favorite;

import com.example.bikerescueusermobile.data.model.request.IRequestService;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class FavoriteRepository {
    private final FavoriteService favoriteService;

    @Inject
    public FavoriteRepository(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    public Single<List<Shop>> getFavoriteShopByUserId(int id){
        return favoriteService.getFavoriteShopByUserId(id, CurrentUser.getInstance().getAccessToken());
    }

    public Single<Favorite> createFavorite(Favorite favorite, boolean status){
        return favoriteService.createFavorite(favorite, status, CurrentUser.getInstance().getAccessToken());
    }
}
