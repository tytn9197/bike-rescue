package com.example.bikerescueusermobile.ui.update_info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;

import butterknife.BindView;

public class UpdateInfoActivity extends BaseActivity {


    @BindView(R.id.actionBar)
    Toolbar actionBar;

    @BindView(R.id.tvMan)
    TextView tvMan;

    @BindView(R.id.tvWoman)
    TextView tvWoman;

    @BindView(R.id.tvContinue)
    TextView tvContinue;

    @BindView(R.id.imgCityy)
    ImageView imgCityy;

    @BindView(R.id.imgDistrict)
    ImageView imgDistrict;

    @BindView(R.id.imgGTTT)
    ImageView imgGTTT;

    @BindView(R.id.imgWard)
    ImageView imgWard;

    @BindView(R.id.imgStreet)
    ImageView imgStreet;

    @BindView(R.id.tvGTTT)
    TextView tvGTTT;

    @BindView(R.id.tvCityy)
    TextView tvCityy;

    @BindView(R.id.tvDistrict)
    TextView tvDistrict;

    @BindView(R.id.tvWard)
    TextView tvWard;

    @BindView(R.id.tvStreet)
    TextView tvStreet;


    //khai báo dữ liệu tạm thời
    String[] distict = {"Quận 1", "Quận 2", "Quận 3", "Quận Gò Vấp", "Quận Tân Phú", "Quận 9"};
    String[] city = {"Hồ Chí Minh", "Hà Nội", "Cần Thơ", "Bình Dương"};
    String[] ward = {"phường 1", "phường 2", "phường 3", "phường 4", "phường 5", "phường 6"};
    String[] street = {"đường số 20", "đường Lê Văn Việt", "đường Hoàng Thiều Hoa", "đường Quang Trung"};
    String[] gttt = {"Giấy căn cước (CMND)", "Giấy phép lái xe"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật thông tin");


        //check box giới tính
        tvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_checked_box_24, 0, 0, 0);
                tvWoman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24, 0, 0, 0);
            }
        });
        tvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24, 0, 0, 0);
                tvWoman.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_checked_box_24, 0, 0, 0);
            }
        });


        //cập nhật thôn tin thành công -> chuyển tới main activity
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Dialog lựa chọn loại GTTT
        imgGTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
                builder.setTitle("Loại GTTT");
                builder.setItems(gttt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvGTTT.setText(gttt[which]);
                        tvGTTT.setTextColor(getResources().getColor(R.color.black));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Dialog lựa chọn Tỉnh/ Thành Phố
        imgCityy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
                builder.setTitle("Chọn Tỉnh/ Thành phố");
                builder.setItems(city, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvCityy.setText(city[which]);
                        tvCityy.setTextColor(getResources().getColor(R.color.black));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Dialog lựa chọn Quận/ Huyện
        imgDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
                builder.setTitle("Chọn Quận/ Huyện");
                builder.setItems(distict, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvDistrict.setText(distict[which]);
                        tvDistrict.setTextColor(getResources().getColor(R.color.black));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Dialog lựa chọn Phường/ Xã
        imgWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
                builder.setTitle("Chọn Phường/ Xã");
                builder.setItems(ward, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvWard.setText(ward[which]);
                        tvWard.setTextColor(getResources().getColor(R.color.black));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //Dialog lựa chọn Đường
        imgStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
                builder.setTitle("Chọn đường");
                builder.setItems(street, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvStreet.setText(street[which]);
                        tvStreet.setTextColor(getResources().getColor(R.color.black));
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_update_info;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Trở lại trang đăng kí

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
