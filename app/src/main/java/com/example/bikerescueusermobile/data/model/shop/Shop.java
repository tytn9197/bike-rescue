package com.example.bikerescueusermobile.data.model.shop;

public class Shop {
    private int id;
    private String shopName;
    private String shopAddress;
    private String shopRatingStar;

    public Shop(int id, String shopName, String shopAddress, String shopRatingStar) {
        this.id = id;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopRatingStar = shopRatingStar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopRatingStar() {
        return shopRatingStar;
    }

    public void setShopRatingStar(String shopRatingStar) {
        this.shopRatingStar = shopRatingStar;
    }
}
