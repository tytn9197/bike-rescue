package com.example.bikerescueusermobile.ui.history;

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

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
    private List<Request> data;
    private HistorySelectedListener listener;

    public HistoryRecyclerViewAdapter(List<Request> viewModel,
                                      HistorySelectedListener selectedListener) {
        data = viewModel;
        this.listener = selectedListener;
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_feed, parent, false);
        return new HistoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_wrapper)
        RelativeLayout feedWrapper;

        @BindView(R.id.txtFeedIdLabel)
        TextView txtFeedIdLabel;

        @BindView(R.id.txtFeedId)
        TextView txtFeedId;

        @BindView(R.id.txtFeedProblem)
        TextView txtFeedProblem;

        @BindView(R.id.imgFeed)
        ImageView imgFeed;

        @BindView(R.id.txtFeedDate)
        TextView txtFeedDate;

        @BindView(R.id.txtFeedStatus)
        TextView txtFeedStatus;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        void bind(Request request) {
            txtFeedDate.setText(MyMethods.convertTimeStampToDate(request.getCreatedDate()) +
                    " lúc " + MyMethods.convertTimeStampToTime(request.getCreatedDate()));
            txtFeedId.setText(request.getRequestCode());
            if (request.getListReqShopService().size() > 0) {
                txtFeedProblem.setText(request.getListReqShopService().get(0).getShopService().getServices().getName());
                if (request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl() != null) {
                    if (request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl().contains("imgur"))
                        Picasso.with(context)
                            .load(request.getListReqShopService().get(0).getShopService().getShops().getAvatarUrl()).placeholder(R.drawable.ic_load)
                            .into(imgFeed);
                }
            }

            RelativeLayout.LayoutParams txtFeedStatusParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtFeedStatusParams.addRule(RelativeLayout.BELOW, R.id.imgFeed);

            txtFeedStatus.setTextColor(Color.parseColor("#0EA92D"));

            if (request.getStatus().equals(MyInstances.STATUS_CREATED)) {
                txtFeedStatus.setTextColor(Color.BLUE);
                txtFeedStatus.setText("Đã gửi");
                txtFeedStatusParams.setMarginEnd(70);
            }

            if (request.getStatus().equals(MyInstances.STATUS_CANCELED)) {
                txtFeedStatus.setText("Đã hủy");
                txtFeedStatus.setTextColor(Color.RED);
                txtFeedStatusParams.setMarginEnd(70);
            }

            if (request.getStatus().equals(MyInstances.STATUS_REJECTED)) {
                txtFeedStatus.setText("Cửa hàng đã từ chối");
                txtFeedStatus.setTextColor(Color.RED);
                txtFeedStatusParams.setMarginEnd(0);
            }

            if (request.getStatus().equals(MyInstances.STATUS_ACCEPT)) {
                txtFeedStatus.setText("Cửa hàng đã nhận");
                txtFeedStatusParams.setMarginEnd(0);
            }

            if (request.getStatus().equals(MyInstances.STATUS_FINISHED)) {
                txtFeedStatus.setText("Yêu cầu đã hoàn thành");
                txtFeedStatusParams.setMarginEnd(0);
            }

            if (request.getStatus().equals(MyInstances.STATUS_ARRIVED)) {
                txtFeedStatus.setText("Thợ đã đến");
                txtFeedStatusParams.setMarginEnd(20);
            }
            txtFeedStatus.setLayoutParams(txtFeedStatusParams);
            feedWrapper.setOnClickListener(v -> listener.onDetailSelected(request));
        }

    }
}
