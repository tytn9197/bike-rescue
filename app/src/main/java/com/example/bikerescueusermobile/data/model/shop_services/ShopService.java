package com.example.bikerescueusermobile.data.model.shop_services;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bikerescueusermobile.data.model.category.Category;
import com.example.bikerescueusermobile.data.model.service.Service;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ShopService implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("serviceName")
    private String name;

    @SerializedName("serviceUrl")
    private String serviceUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private boolean status;

    @SerializedName("category")
    private Category category;

    public ShopService(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public ShopService(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShopService() {
    }

    protected ShopService(Parcel in) {
        id = in.readInt();
        name = in.readString();
        serviceUrl = in.readString();
        description = in.readString();
        status = in.readByte() != 0;
    }

    public static final Creator<ShopService> CREATOR = new Creator<ShopService>() {
        @Override
        public ShopService createFromParcel(Parcel in) {
            return new ShopService(in);
        }

        @Override
        public ShopService[] newArray(int size) {
            return new ShopService[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(serviceUrl);
        dest.writeString(description);
        dest.writeByte((byte) (status ? 1 : 0));
    }
}
