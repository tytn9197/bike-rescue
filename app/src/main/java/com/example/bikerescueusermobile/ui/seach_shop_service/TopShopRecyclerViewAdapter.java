package com.example.bikerescueusermobile.ui.seach_shop_service;

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
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopShopRecyclerViewAdapter  extends RecyclerView.Adapter<TopShopRecyclerViewAdapter.ViewHolder>{
    private List<Shop> data = new ArrayList<>();
    private TopShopSelectedListener listener;

    public TopShopRecyclerViewAdapter(List<Shop> viewModel,
                                       LifecycleOwner lifecycleOwner, TopShopSelectedListener selectedListener) {
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

        private Shop shop;


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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Shop shop) {
            this.shop = shop;

            txtTopShopName.setText(shop.getShopName());

            txtTopShopShopAddress.setText(shop.getAddress());

            String txtNumOfStar = shop.getShopRatingStar() + "/" + topShopRatingBar.getNumStars();
            txtTopShopNumberOfStar.setText(txtNumOfStar);

            topShopRatingBar.setRating(Float.parseFloat(shop.getShopRatingStar()));


            wrapper.setOnClickListener((View.OnClickListener) v -> listener.onDetailSelected(shop));
        }

    }
}