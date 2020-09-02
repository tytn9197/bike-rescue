package com.example.bikerescueusermobile.ui.favorite;

import com.example.bikerescueusermobile.data.model.shop.Shop;

public interface FavoriteSelectedListener{
    void onDetailSelected(Shop shop);
    void onFavoriteSelected(Shop shop, float rating);
}
