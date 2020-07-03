package com.example.bikerescueusermobile.ui.seach_shop_service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopServiceListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<ShopService> listAllShopServicesName;

    public ShopServiceListViewAdapter(Context context, List<ShopService> listAllShopServicesName) {
        mContext = context;
        this.listAllShopServicesName = listAllShopServicesName;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        TextView serviceName;
    }

    @Override
    public int getCount() {
        return listAllShopServicesName.size();
    }

    @Override
    public ShopService getItem(int position) {
        return listAllShopServicesName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.serviceName = view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.serviceName.setText(listAllShopServicesName.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText, List<ShopService> list) {
        charText = charText.toLowerCase(Locale.getDefault());
        listAllShopServicesName = new ArrayList<>();
        if (charText.length() == 0) {
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listAllShopServicesName.add(list.get(i));
                }
            }
        }
        notifyDataSetChanged();

    }

}
