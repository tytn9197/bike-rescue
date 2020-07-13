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
import com.example.bikerescueusermobile.data.model.request.Request;
import com.example.bikerescueusermobile.ui.shop_owner.ShopRequestDetailActivity;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    private List<Request> requestList;
    private Context context;
    private OnRequestListener mOnRequestListener;


    public CustomAdapter(List<Request> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    public CustomAdapter(List<Request> requestList, Context context, OnRequestListener mOnRequestListener) {
        this.requestList = requestList;
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


        holder.imageView.setImageResource(R.drawable.ic_user);
        holder.request_code.setText(requestList.get(position).getRequest_code());
        holder.name.setText(requestList.get(position).getCreated().getFullName());
        holder.address.setText(requestList.get(position).getAddress());
        holder.field.setText("Sữa chữa xe máy");
        holder.time.setText(requestList.get(position).getTime().toString());
        if(requestList.get(position).getStatus() == "DONE"){
            holder.ic_status.setImageResource(R.drawable.ic_complete);
        } else if(requestList.get(position).getStatus() == "CANCEL"){
            holder.ic_status.setImageResource(R.drawable.ic_hand);
        }
        holder.status.setText(requestList.get(position).getStatus());



    }

    @Override
    public int getItemCount() {
        return requestList.size();
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
