package com.example.bikerescueusermobile.data.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.shop_request.Order;
import com.example.bikerescueusermobile.ui.shop_owner.ShopRequestDetailActivity;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    private List<Order> orderList;
    private Context context;
    private OnRequestListener mOnRequestListener;


    public CustomAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public CustomAdapter(List<Order> orderList, Context context, OnRequestListener mOnRequestListener) {
        this.orderList = orderList;
        this.context = context;
        this.mOnRequestListener = mOnRequestListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.shop_request, parent, false);
        return new MyViewHolder(view, mOnRequestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.imageView.setImageResource(orderList.get(position).getImage());
        holder.request_code.setText(orderList.get(position).getRequest_code());
        holder.name.setText(orderList.get(position).getName());
        holder.address.setText(orderList.get(position).getAddress());
        holder.field.setText(orderList.get(position).getField());
        holder.time.setText(orderList.get(position).getTime());
        holder.ic_status.setImageResource(orderList.get(position).getStatus_ic());
        holder.status.setText(orderList.get(position).getStatus());



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView request_code;
        TextView name;
        TextView address;
        TextView field;
        TextView time;
        ImageView ic_status;
        TextView status;
        OnRequestListener onRequestListener;

        public MyViewHolder(@NonNull View itemView, OnRequestListener onRequestListener) {
            super(itemView);

            //define
            imageView = itemView.findViewById(R.id.imageId);
            request_code = itemView.findViewById(R.id.request_code);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.request_address);
            field = itemView.findViewById(R.id.request_problem);
            time = itemView.findViewById(R.id.request_time);
            ic_status = itemView.findViewById(R.id.ic_status);
            status = itemView.findViewById(R.id.request_status);


            //
            this.onRequestListener = onRequestListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onRequestListener.onRequestClick(getAdapterPosition());
            Intent intent = new Intent(context.getApplicationContext(), ShopRequestDetailActivity.class);
            context.startActivity(intent);

//            Toast.makeText(context, "Item: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnRequestListener {
        void onRequestClick(int position);
    }
}
