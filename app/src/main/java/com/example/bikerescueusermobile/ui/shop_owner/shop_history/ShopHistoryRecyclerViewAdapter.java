package com.example.bikerescueusermobile.ui.shop_owner.shop_history;

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
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ShopHistoryRecyclerViewAdapter.ViewHolder> {
    private List<Request> data;
    private ShopHistorySelectedListener listener;

    public ShopHistoryRecyclerViewAdapter(List<Request> data, ShopHistorySelectedListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_request, parent, false);
        return new ShopHistoryRecyclerViewAdapter.ViewHolder(view);
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

        @BindView(R.id.shop_feed_wrapper)
        RelativeLayout shopFeedWrapper;

        @BindView(R.id.txtShopFeedIdLabel)
        TextView txtShopFeedIdLabel;

        @BindView(R.id.txtShopFeedId)
        TextView txtShopFeedId;

        @BindView(R.id.txtShopFeedProblem)
        TextView txtShopFeedProblem;

        @BindView(R.id.txtShopCustomer)
        TextView txtShopCustomer;

        @BindView(R.id.imgShopFeed)
        ImageView imgShopFeed;

        @BindView(R.id.txtShopFeedDate)
        TextView txtShopFeedDate;

        @BindView(R.id.txtShopFeedStatus)
        TextView txtShopFeedStatus;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        void bind(Request request) {
            txtShopCustomer.setText(request.getCreatedUser().getFullName());
            txtShopFeedDate.setText(MyMethods.convertTimeStampToDate(request.getCreatedDate()) +
                    " lúc " + MyMethods.convertTimeStampToTime(request.getCreatedDate()));
            txtShopFeedId.setText(request.getRequestCode());
            txtShopFeedProblem.setText(request.getListReqShopService().get(0).getShopService().getServices().getName());

            RelativeLayout.LayoutParams txtShopFeedStatusParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtShopFeedStatusParams.addRule(RelativeLayout.BELOW, R.id.imgShopFeed);

            if (request.getCreatedUser().getAvatarUrl() != null) {
                Picasso.with(context)
                        .load(request.getCreatedUser().getAvatarUrl()).placeholder(R.drawable.ic_load)
                        .into(imgShopFeed);
            }

            txtShopFeedStatus.setTextColor(Color.parseColor("#0EA92D"));


            if (request.getStatus().equals(MyInstances.STATUS_CANCELED)) {
                txtShopFeedStatus.setText("Đã hủy");
                txtShopFeedStatus.setTextColor(Color.RED);
                txtShopFeedStatusParams.setMarginEnd(70);
            }


            if (request.getStatus().equals(MyInstances.STATUS_FINISHED)) {
                txtShopFeedStatus.setText("Yêu cầu đã hoàn thành");
                txtShopFeedStatusParams.setMarginEnd(0);
            }

            txtShopFeedStatus.setLayoutParams(txtShopFeedStatusParams);
            shopFeedWrapper.setOnClickListener(v -> listener.onDetailSelected(request));
        }

    }
}
