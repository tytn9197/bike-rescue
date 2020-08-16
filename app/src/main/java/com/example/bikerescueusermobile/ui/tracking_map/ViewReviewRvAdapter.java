package com.example.bikerescueusermobile.ui.tracking_map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.request.ReviewRequestDTO;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.util.MyMethods;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewReviewRvAdapter extends RecyclerView.Adapter<ViewReviewRvAdapter.ViewHolder>{
    private List<ReviewRequestDTO> data = new ArrayList<>();

    public ViewReviewRvAdapter(List<ReviewRequestDTO> viewModel) {
        data = viewModel;
    }

    @NonNull
    @Override
    public ViewReviewRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_reviews, parent, false);
        return new ViewReviewRvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewReviewRvAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviewImg)
        ImageView reviewImg;

        @BindView(R.id.txtReviewUserName)
        TextView txtReviewUserName;

        @BindView(R.id.txtReviewService)
        TextView txtReviewService;

        @BindView(R.id.reviewRating)
        ScaleRatingBar reviewRating;

        @BindView(R.id.txtReviewComment)
        TextView txtReviewComment;

        @BindView(R.id.txtReviewDate)
        TextView txtReviewDate;

        @BindView(R.id.txtTittle)
        TextView txtTittle;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        void bind(ReviewRequestDTO review) {
            if(review.getCreated().getAvatarUrl().contains("imgur")){
                Picasso.with(context)
                        .load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load)
                        .into(reviewImg);
            }

            txtReviewUserName.setText(review.getCreated().getFullName());

            txtReviewService.setText("Người dùng đã sử dụng dịch vụ: " +
                    review.getListReqShopService().get(0).getShopService().getServices().getName());

            reviewRating.setStepSize((float) 0.5);
            reviewRating.setRating(review.getReviewRating().floatValue());

            if(review.getReviewComment().equals("")){
                txtTittle.setVisibility(View.GONE);
                txtReviewComment.setVisibility(View.GONE);
            }else{
                txtTittle.setVisibility(View.VISIBLE);
                txtReviewComment.setVisibility(View.VISIBLE);
                txtReviewComment.setText(review.getReviewComment());
            }

            txtReviewDate.setText("Vao luc "+
                    MyMethods.convertTimeStampToTime(review.getReviewUpdateDate()) +
                    " ngay " +
                    MyMethods.convertTimeStampToDate(review.getReviewUpdateDate()));
        }

    }
}