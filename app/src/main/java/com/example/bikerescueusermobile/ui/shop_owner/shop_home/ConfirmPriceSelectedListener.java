package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import com.example.bikerescueusermobile.data.model.request.RequestShopService;

public interface ConfirmPriceSelectedListener {
    void onDeleteClick(RequestShopService requestShopService);
    void onChangeQuantityClick(RequestShopService requestShopService);
}
