package com.fotuoc.techpress;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class DevActivity extends AppCompatActivity {

    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(DevActivity.this, ProfileActivity.class);
                startActivity(intent);
                // Optionally, finish this activity if you don't want to come back to it
                // finish();
            }
        });

        // You can dynamically set the developer info here if needed
        // TextView textViewName = findViewById(R.id.textViewName);
        // textViewName.setText("Name: Your Real Name");
        // ... and so on for other TextViews
    }

}