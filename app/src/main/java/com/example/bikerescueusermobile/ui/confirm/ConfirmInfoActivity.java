package com.example.bikerescueusermobile.ui.confirm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseActivity;
import com.example.bikerescueusermobile.ui.create_request.CreateRequestActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.map.MapActivity;
import com.example.bikerescueusermobile.ui.register.CreatePasswordActivity;
import com.example.bikerescueusermobile.ui.send_request.SendRequestActivity;
import com.example.bikerescueusermobile.ui.update_info.UpdateInfoActivity;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConfirmInfoActivity extends BaseActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
//    @BindView(R.id.spinner1)
//    Spinner problem;
//
//    @BindView(R.id.spinner2)
//    Spinner bike;
//
//    @BindView(R.id.spinner3)
//    Spinner year;


    @BindView(R.id.confirm_toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvProblem)
    TextView tvProblem;

    @BindView(R.id.tvBrand)
    TextView tvBrand;

    @BindView(R.id.tvYear)
    TextView tvYear;

    @BindView(R.id.btnBookService)
    TextView tvBookService;

    @BindView(R.id.btnImg)
    ImageButton btnImg;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtMarker)
    EditText edtMarker;

    @Override
    protected int layoutRes() {
        return R.layout.activity_confirm_info;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Xác nhận thông tin");



        //test data
        edtName.setText("Phan Gia Cường");
        edtPhone.setText("0865137822");
        edtMarker.setText("206/16 đường số 20 phường 5 quận Gò Vấp");



        tvProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                builder.setTitle("Vấn đề của bạn");
                String[] problems = getBaseContext().getResources().getStringArray(R.array.vande);
                builder.setItems(problems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvProblem.setText(problems[which]);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                builder.setTitle("Hãng xe");
                String[] brands = getBaseContext().getResources().getStringArray(R.array.hangxe);
                builder.setItems(brands, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvBrand.setText(brands[which]);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmInfoActivity.this);
                builder.setTitle("Đời xe");
                String[] year = getBaseContext().getResources().getStringArray(R.array.doixe);
                builder.setItems(year, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "city: " + city[which], Toast.LENGTH_SHORT).show();
                        tvYear.setText(year[which]);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        tvBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ConfirmInfoActivity.this, SweetAlertDialog.NORMAL_TYPE);
                sweetAlertDialog.setTitleText("Thông báo");
                sweetAlertDialog.setContentText("Quý khách xác nhận thực hiện gọi dịch vụ lúc này?");
                sweetAlertDialog.setConfirmText("Xác nhận");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(getApplicationContext(), CreateRequestActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                sweetAlertDialog.setCancelButton("Quay lại", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();

            }
        });

//        btnImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (imageIntent.resolveActivity(getParent().getPackageManager())!=null)
//                {
//                    startActivityForResult(imageIntent,REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });



    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBipmap = (Bitmap) extras.get("data");
//            btnImg.setImageBitmap(imageBipmap);
//
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            //Trở lại trang gọi dịch vụ

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
