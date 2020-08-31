package com.example.bikerescueusermobile.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.base.BaseFragment;
import com.example.bikerescueusermobile.data.model.user.CurrentUser;
import com.example.bikerescueusermobile.ui.login.LoginActivity;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.example.bikerescueusermobile.ui.shop_owner.ShopUpdateInfoActivity;
import com.example.bikerescueusermobile.util.MyInstances;
import com.example.bikerescueusermobile.util.MyMethods;
import com.example.bikerescueusermobile.util.SharedPreferenceHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {
    @Override
    protected int layoutRes() {
        return R.layout.biker_profile_fragment;
    }

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.txtFullName)
    TextView txtFullName;

    @BindView(R.id.txtUserEmail)
    TextView txtUserEmail;

    @BindView(R.id.txtUserPhoneNumber)
    TextView txtUserPhoneNumber;

    @BindView(R.id.txtUserAddress)
    TextView txtUserAddress;

    @BindView(R.id.txtUserCreateDate)
    TextView txtUserCreateDate;

    @BindView(R.id.avatar)
    CircleImageView avatar;

    @BindView(R.id.logout)
    TextView logout;

    @BindView(R.id.btnUpdateProfile)
    Button btnUpdateProfile;

    @BindView(R.id.btnAddVehicle)
    Button btnAddVehicle;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_LOGGED_IN, "");
            Intent intentLog = new Intent(getActivity(), LoginActivity.class);
            SharedPreferenceHelper.setSharedPreferenceString(getActivity(), MyInstances.KEY_COUNT_CANCELATION, "");
            startActivity(intentLog);
            getActivity().finish();
        });

        txtFullName.setText(CurrentUser.getInstance().getFullName());
        txtUserEmail.setText(CurrentUser.getInstance().getEmail());
        txtUserPhoneNumber.setText(CurrentUser.getInstance().getPhoneNumber());
        txtUserAddress.setText(CurrentUser.getInstance().getAddress());

        String date = CurrentUser.getInstance().getCreatedTime();
        txtUserCreateDate.setText((date.split(" "))[0]);


        if (CurrentUser.getInstance().getAvatarUrl().contains("imgur")) {
            Picasso.with(getActivity()).load(CurrentUser.getInstance().getAvatarUrl()).placeholder(R.drawable.ic_load).into(avatar);
        }

        avatar.setOnClickListener(v -> {
            ImagePicker.Companion.with(getActivity())
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ShopUpdateInfoActivity.class);
            startActivity(intent);
        });

        btnAddVehicle.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), VehicleActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //get File object from ImagePicker
            File file = ImagePicker.Companion.getFile(data);

            // set path toi' hin`h do' thanh` bitmap
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


            String s = MyMethods.bitmapToString(myBitmap);
            Log.e(TAG, "b -> string " + s);

            //set image to image view

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e(TAG, "ImagePicker - Get image fail: " + ImagePicker.Companion.getError(data));
        } else {
            Log.e(TAG, "ImagePicker - Task Cancelled");
        }
    }
}
