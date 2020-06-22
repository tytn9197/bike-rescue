package com.example.bikerescueusermobile.ui.send_request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bikerescueusermobile.R;

public class FragmentStep1 extends Fragment {
    View view;
    Spinner spinner;
    Button btn;
    SelectTab selectTab;


    // TODO: Externalize string-array
    public FragmentStep1(SelectTab selectTab) {
            this.selectTab = selectTab;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.step1_fragment,container,false);
            btn = (Button) view.findViewById(R.id.btnTieptheo);
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                        selectTab.selectTab(1);
               }
           });
        spinner = view.findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources()
                .getStringArray(R.array.vande));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(myAdapter);
        return view;
    }

}
