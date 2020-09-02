package com.example.bikerescueusermobile.ui.favorite;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.shop.Shop;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecyclerViewAdapter  extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder>{
    private List<Shop> data;
    private FavoriteSelectedListener listener;

    public FavoriteRecyclerViewAdapter(List<Shop> viewModel,
                                      FavoriteSelectedListener selectedListener) {
        data = viewModel;
        this.listener = selectedListener;

    }

    @NonNull
    @Override
    public FavoriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_favorite, parent, false);
        return new FavoriteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favorite_wrapper)
        RelativeLayout favoriteWrapper;

        @BindView(R.id.txtFavoriteShopName)
        TextView txtFavoriteShopName;

        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;

        @BindView(R.id.txtFavoriteShopAddress)
        TextView txtFavoriteShopAddress;

        @BindView(R.id.txtFavoriteNumberOfStar)
        TextView txtFavoriteNumberOfStar;

        @BindView(R.id.favoriteRatingBar)
        ScaleRatingBar favoriteRatingBar;

        @BindView(R.id.savedShopRatingBar)
        ScaleRatingBar savedShopRatingBar;

        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        void bind(Shop shop) {
            txtFavoriteShopName.setText(shop.getShopName());

            txtFavoriteShopAddress.setText(shop.getAddress());

            String txtNumOfStar = shop.getShopRatingStar() + "/" + favoriteRatingBar.getNumStars();
            txtFavoriteNumberOfStar.setText(txtNumOfStar);

            if(shop.getAvatarUrl().contains("imgur")){
                Picasso.with(context)
                        .load(shop.getAvatarUrl()).placeholder(R.drawable.ic_load)
                        .into(imgFavorite);
            }

            favoriteRatingBar.setRating(Float.parseFloat(shop.getShopRatingStar()));

            favoriteWrapper.setOnClickListener((View.OnClickListener) v -> listener.onDetailSelected(shop));

            savedShopRatingBar.setRating(1);
            savedShopRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                    listener.onFavoriteSelected(shop, rating);
                }
            });
        }

    }
}
