package com.fotuoc.techpress;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash); // Set your splash screen layout

        mAuth = FirebaseAuth.getInstance();

        // Use a Handler to delay the transition to the next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // User is signed in, navigate to Main Activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // No user signed in, navigate to Auth/Login Activity
                Intent intent = new Intent(SplashActivity.this, AuthActivity.class); // Or LoginActivity
                startActivity(intent);
            }
            finish(); // Finish SplashActivity so user can't go back to it
        }, 3000); // 3-second delay (adjust as needed)
    }
}