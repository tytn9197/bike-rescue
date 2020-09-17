package com.example.bikerescueusermobile.ui.shop_owner.shop_home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.request.RequestShopService;
import com.example.bikerescueusermobile.util.MyMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmPriceRecyclerViewAdapter extends RecyclerView.Adapter<ConfirmPriceRecyclerViewAdapter.ViewHolder>{
    private List<RequestShopService> data;
    private ConfirmPriceSelectedListener listener;
    private boolean isReview;

    public ConfirmPriceRecyclerViewAdapter(List<RequestShopService> viewModel, boolean isReview,
                                       ConfirmPriceSelectedListener selectedListener) {
        data = viewModel;
        this.listener = selectedListener;
        this.isReview = isReview;
    }

    @NonNull
    @Override
    public ConfirmPriceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_confirm_price, parent, false);
        return new ConfirmPriceRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmPriceRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        @BindView(R.id.txtServiceName)
        TextView txtServiceName;

        @BindView(R.id.txtUnitPrice)
        TextView txtUnitPrice;

        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        @BindView(R.id.txtPrice)
        TextView txtPrice;

        @BindView(R.id.btnDeleteService)
        Button btnDeleteService;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        void bind(RequestShopService requestShopService) {
            txtServiceName.setText("Dịch vụ: " + requestShopService.getShopService().getServices().getName());
            txtUnitPrice.setText("" + MyMethods.convertMoney(requestShopService.getShopService().getPrice().floatValue() * 1000)
                    + " vnd / " + requestShopService.getShopService().getServices().getUnit());
            txtQuantity.setText("" + requestShopService.getQuantity());
            txtPrice.setText("" + MyMethods.convertMoney(requestShopService.getShopService().getPrice().floatValue() * 1000 * requestShopService.getQuantity())+ " vnd");
            btnDeleteService.setOnClickListener(v -> listener.onDeleteClick(requestShopService));

            if(isReview){
                btnDeleteService.setVisibility(View.GONE);
            }
        }

    }
}
