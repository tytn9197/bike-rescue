package com.example.bikerescueusermobile.ui.otp_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bikerescueusermobile.R;
import com.example.bikerescueusermobile.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginByPhoneNumberActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonContinue).setOnClickListener(v -> {
            String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

            String number = editText.getText().toString().trim();

            if (number.isEmpty() || number.length() < 10) {
                editText.setError("Valid number is required");
                editText.requestFocus();
                return;
            }

            String phoneNumber = "+" + code + number;

            Intent intent = new Intent(LoginByPhoneNumberActivity.this, VerifyActivity.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}