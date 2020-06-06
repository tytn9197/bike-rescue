package com.example.bikerescueusermobile.ui.history;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.request.BikerRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
    private List<BikerRequest> data = new ArrayList<>();
    private HistorySelectedListener listener;

    public HistoryRecyclerViewAdapter(List<BikerRequest> viewModel,
                                      LifecycleOwner lifecycleOwner,
                                      HistorySelectedListener selectedListener) {
//        viewModel.getFeedLivedata().observe(lifecycleOwner, cases -> {
//            data.clear();
//            if (cases != null) {
//                data.addAll(cases);
//                notifyDataSetChanged();
//            }
//        });
//        setHasStableIds(true);
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

        private BikerRequest feed;

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(BikerRequest feed) {
            this.feed = feed;
            txtFeedDate.setText(feed.getCreatedTime());
            txtFeedId.setText("0000000" + feed.getId());
            txtFeedProblem.setText(feed.getName());

            RelativeLayout.LayoutParams txtFeedStatusParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtFeedStatusParams.addRule(RelativeLayout.BELOW,R.id.imgFeed);

            if(feed.getStatus() == 2){
                txtFeedStatus.setTextColor(Color.parseColor("#F55D30"));
                txtFeedStatus.setText("Đang xử lí");
                txtFeedStatusParams.setMarginEnd(50);
            }else if(feed.getStatus() == 3){
                txtFeedStatus.setTextColor(Color.parseColor("#dd2c00"));
                txtFeedStatus.setText("Đã huỷ");
                txtFeedStatusParams.setMarginEnd(70);

            }else if(feed.getStatus() == 5) {
                txtFeedStatus.setTextColor(Color.parseColor("#0EA92D"));
                txtFeedStatus.setText("Đã hoàn thành");
                txtFeedStatusParams.setMarginEnd(13);
            }
            txtFeedStatus.setLayoutParams(txtFeedStatusParams);

//            }else if(feed.getStatus() == 1){
//                txtFeedStatus.setTextColor(Color.parseColor("#B3B3B3"));
//                txtFeedStatus.setText("Chưa được xác nhận");
//            }            }else if(feed.getStatus() == 4) {
//                txtFeedStatus.setTextColor(Color.parseColor("#F5C330"));
//                txtFeedStatus.setText("Cần được hỗ trợ");


//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//            try {
//                Date dateFromStr = df.parse(feed.getCreatedTime());
//                txtFeedDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateFromStr));
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
            feedWrapper.setOnClickListener((View.OnClickListener) v -> listener.onDetailSelected(feed));
        }

    }
}
