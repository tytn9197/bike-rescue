package com.example.bikerescueusermobile.data.model.shop_services;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ShopServiceTable implements Parcelable{

    @SerializedName("id")
    private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("minPrice")
    private Double minPrice;

    @SerializedName("maxPrice")
    private Double maxPrice;

    @SerializedName("serviceAvatar")
    private String serviceAvatar;

    @SerializedName("status")
    private boolean status;

    @SerializedName("services")
    private ShopService services;

    @SerializedName("shops")
    private Shop shops;

    public ShopServiceTable() {
    }

    protected ShopServiceTable(Parcel in) {
        id = in.readInt();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            minPrice = null;
        } else {
            minPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            maxPrice = null;
        } else {
            maxPrice = in.readDouble();
        }
        serviceAvatar = in.readString();
        status = in.readByte() != 0;
        services = in.readParcelable(ShopService.class.getClassLoader());
    }

    public static final Creator<ShopServiceTable> CREATOR = new Creator<ShopServiceTable>() {
        @Override
        public ShopServiceTable createFromParcel(Parcel in) {
            return new ShopServiceTable(in);
        }

        @Override
        public ShopServiceTable[] newArray(int size) {
            return new ShopServiceTable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (minPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minPrice);
        }
        if (maxPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(maxPrice);
        }
        dest.writeString(serviceAvatar);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeParcelable(services, flags);
    }
}
