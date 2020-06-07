package com.example.bikerescueusermobile.ui.send_request;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bikerescueusermobile.R;

public class FragmentStep3 extends Fragment implements View.OnClickListener {
    View view;
    private ImageButton mimageButton1;
    private static final int REQUEST_IMAGE_CAPTURE = 101;



    public FragmentStep3() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.step3_fragment,container,false);
            mimageButton1 = (ImageButton) view.findViewById(R.id.imgBtn);
            mimageButton1.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtn:
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (imageIntent.resolveActivity(getActivity().getPackageManager())!=null)
                {
                    startActivityForResult(imageIntent,REQUEST_IMAGE_CAPTURE);
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE){
            Bundle extras = data.getExtras();
            Bitmap imageBipmap = (Bitmap) extras.get("data");
            mimageButton1.setImageBitmap(imageBipmap);

        }
    }
}
