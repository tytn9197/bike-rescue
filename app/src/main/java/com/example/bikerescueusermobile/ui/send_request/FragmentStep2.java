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

public class FragmentStep2 extends Fragment {
    View view;
    Spinner spinner1;
    Spinner spinner2;
    Button button;
    SelectTab selectTab;

    public FragmentStep2(SelectTab selectTab) {
            this.selectTab = selectTab;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.step2_fragment,container,false);
            button = (Button) view.findViewById(R.id.btnTieptheo);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTab.selectTab(2);
                }
            });
        spinner1 = view.findViewById(R.id.spinner2);
        spinner2 = view.findViewById(R.id.spinner3);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources()
                .getStringArray(R.array.hangxe));
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,getResources()
                .getStringArray(R.array.doixe));


        myAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        myAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(myAdapter1);
        spinner2.setAdapter(myAdapter2);
        return view;
    }
}
