package com.example.bikerescueusermobile.ui.seach_shop_service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopShopRecyclerViewAdapter  extends RecyclerView.Adapter<TopShopRecyclerViewAdapter.ViewHolder>{
    private List<Shop> data;
    private TopShopSelectedListener listener;

    public TopShopRecyclerViewAdapter(List<Shop> viewModel,
                                      TopShopSelectedListener selectedListener) {
        data = viewModel;
        this.listener = selectedListener;
    }

    @NonNull
    @Override
    public TopShopRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_top_shop, parent, false);
        return new TopShopRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopShopRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.top_shop_wrapper)
        RelativeLayout wrapper;

        @BindView(R.id.txtTopShopName)
        TextView txtTopShopName;

        @BindView(R.id.imgTopShop)
        ImageView imgTopShop;

        @BindView(R.id.txtTopShopShopAddress)
        TextView txtTopShopShopAddress;

        @BindView(R.id.txtTopShopNumberOfStar)
        TextView txtTopShopNumberOfStar;

        @BindView(R.id.topShopRatingBar)
        ScaleRatingBar topShopRatingBar;

        @BindView(R.id.txtDistance)
        TextView txtDistance;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        void bind(Shop shop) {
            txtTopShopName.setText(shop.getShopName());

            txtTopShopShopAddress.setText(shop.getAddress());

            String txtNumOfStar = shop.getShopRatingStar() + "/" + topShopRatingBar.getNumStars();
            txtTopShopNumberOfStar.setText(txtNumOfStar);

            topShopRatingBar.setRating(Float.parseFloat(shop.getShopRatingStar()));
            txtDistance.setText(String.format(Locale.getDefault(),"Cách đây %.1f km", shop.getDistanceFromUser()));

            wrapper.setOnClickListener(v -> listener.onDetailSelected(shop));

            if (shop.getAvtUrl() != null) {
                Picasso.with(context)
                        .load(shop.getAvtUrl()).placeholder(R.drawable.ic_load)
                        .into(imgTopShop);
            }
        }

    }
}