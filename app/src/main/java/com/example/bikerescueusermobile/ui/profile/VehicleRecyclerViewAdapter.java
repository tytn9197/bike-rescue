package com.example.bikerescueusermobile.ui.profile;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.data.model.vehicle.Vehicle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter<VehicleRecyclerViewAdapter.ViewHolder> {
    private List<Vehicle> data;
    private VehicleSelectedListener listener;

    public VehicleRecyclerViewAdapter(List<Vehicle> viewModel,
                                      VehicleSelectedListener selectedListener) {
        data = viewModel;
        this.listener = selectedListener;
    }

    @NonNull
    @Override
    public VehicleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_vehicle, parent, false);
        return new VehicleRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.txtVehicleId)
//        TextView txtVehicleId;

        @BindView(R.id.vehicleWrapper)
        LinearLayout vehicleWrapper;

        @BindView(R.id.txtVehicleBrand)
        TextView txtVehicleBrand;

        @BindView(R.id.txtVehicleYear)
        TextView txtVehicleYear;

        @BindView(R.id.txtVehicleType)
        TextView txtVehicleType;

        @BindView(R.id.edtServiceStatus)
        EditText edtServiceStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void bind(Vehicle vehicle) {

//            txtVehicleId.setText("Id: " + vehicle.getId());
            txtVehicleBrand.setText("Tên xe: " + vehicle.getBrand());
            txtVehicleYear.setText("Đời xe: " + vehicle.getVehiclesYear());
            txtVehicleType.setText("Loại xe: " + vehicle.getType());

            if(vehicle.isStatus()){
                edtServiceStatus.setText("Trạng thái: Hoạt động");
                edtServiceStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_active, 0);
            }else{
                edtServiceStatus.setText("Trạng thái: Không hoạt động");
                edtServiceStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_inactive, 0);
            }

            vehicleWrapper.setOnClickListener(v -> listener.onDetailSelected(vehicle));
            vehicleWrapper.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(vehicle);
                    return true;
                }
            });
        }

    }
}
