package com.example.bikerescueusermobile.ui.shop_owner.shop_chart;

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
import com.example.bikerescueusermobile.data.model.service.CountingService;
import com.example.bikerescueusermobile.data.model.shop_services.ShopServiceTable;
import com.example.bikerescueusermobile.ui.shop_owner.services.ServiceRecycleViewAdapter;
import com.example.bikerescueusermobile.ui.shop_owner.services.ShopServiceSelectedListener;
import com.example.bikerescueusermobile.util.MyMethods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceCoutingRecyclerViewAdapter extends RecyclerView.Adapter<ServiceCoutingRecyclerViewAdapter.ViewHolder> {
    private List<CountingService> data;
    private boolean isPrice;

    public ServiceCoutingRecyclerViewAdapter(List<CountingService> data, boolean isPrice) {
        this.data = data;
        this.isPrice = isPrice;
    }

    @NonNull
    @Override
    public ServiceCoutingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_counting_service, parent, false);
        return new ServiceCoutingRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCoutingRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @BindView(R.id.txtCountingNumber)
        TextView txtCountingNumber;

        @BindView(R.id.txtCountingName)
        TextView txtCountingName;

        @SuppressLint("SetTextI18n")
        void bind(CountingService countingService) {
            if(!isPrice) {
                txtCountingName.setText(countingService.getServiceName());
                txtCountingNumber.setText("" + countingService.getCountRequest());
            } else {
                txtCountingName.setText(countingService.getServiceName());
                txtCountingNumber.setText("" + MyMethods.convertMoney(countingService.getCountRequest()*1000) + " vnd");
            }
        }
    }
}
