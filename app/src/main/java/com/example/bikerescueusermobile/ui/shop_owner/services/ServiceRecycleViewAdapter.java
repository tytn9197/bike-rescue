package com.example.bikerescueusermobile.ui.shop_owner.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.shop_services.ShopService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceRecycleViewAdapter extends RecyclerView.Adapter<ServiceRecycleViewAdapter.ViewHolder> {
    private List<ShopServiceTable> data;
    private ShopServiceSelectedListener listener;

    public ServiceRecycleViewAdapter(List<ShopServiceTable> data, ShopServiceSelectedListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_service, parent, false);
        return new ServiceRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_service_feed_wrapper)
        RelativeLayout shopServiceFeedWrapper;

        @BindView(R.id.txtServiceName)
        TextView txtServiceName;

        @BindView(R.id.txtCategory)
        TextView txtCategory;

        @BindView(R.id.imgServiceFeed)
        ImageView imgServiceFeed;

        @BindView(R.id.txtServicePriceLabel)
        TextView txtServicePriceLabel;

        @BindView(R.id.txtServicePrice)
        TextView txtServicePrice;

        @BindView(R.id.txtUnitLabel)
        TextView txtUnitLabel;

        @BindView(R.id.txtUnit)
        TextView txtUnit;

        @BindView(R.id.txtStatus)
        TextView txtStatus;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        void bind(ShopServiceTable shopServiceTable) {
            txtServiceName.setText(shopServiceTable.getServices().getName());
            txtCategory.setText(shopServiceTable.getServices().getCategory().getCategoryName());
            txtServicePrice.setText(shopServiceTable.getPrice().toString());
            txtUnit.setText("/ " + shopServiceTable.getUnit());

            RelativeLayout.LayoutParams txtServiceFeedStatusParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtServiceFeedStatusParams.addRule(RelativeLayout.BELOW, R.id.imgServiceFeed);

            if (shopServiceTable.getServiceAvatar() != null) {
                Picasso.with(context)
                        .load(shopServiceTable.getServiceAvatar()).placeholder(R.drawable.ic_load)
                        .into(imgServiceFeed);
            }


            if(shopServiceTable.getServices().isStatus() == true){
                txtStatus.setText("Kích hoạt");
                txtStatus.setTextColor(Color.parseColor("#0EA92D"));
            }
            txtStatus.setLayoutParams(txtServiceFeedStatusParams);
            shopServiceFeedWrapper.setOnClickListener(v -> listener.onDetailSelected(shopServiceTable));
        }
    }
}
