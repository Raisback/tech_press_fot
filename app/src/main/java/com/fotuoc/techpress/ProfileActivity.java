package com.fotuoc.techpress;


import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth; // Import Firebase Auth

public class ProfileActivity extends AppCompatActivity implements EditProfileDialogFragment.EditProfileDialogListener {

    private TextView textViewUserName;
    private TextView textViewUserEmail;

    // Firebase Auth instance
    private FirebaseAuth mAuth;

    // Example user data (in a real app, this would come from a database/API)
    private String currentUserName = "Jane Doe";
    private String currentUserEmail = "jane.doe@foodapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        ImageView profileImageView = findViewById(R.id.profileImageView);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        TextView btnEditProfile = findViewById(R.id.btnEditProfile);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button btnAbout = findViewById(R.id.btnDev);

        // Populate User Data
        loadUserProfileData();

        // --- Set up Click Listeners ---
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        btnSignOut.setOnClickListener(v -> {
            showSignOutConfirmationDialog(); // Call the new confirmation dialog
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DevActivity.class);
            startActivity(intent);
        });

        profileImageView.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Profile image clicked!", Toast.LENGTH_SHORT).show();
            // TODO: Implement logic to pick a new profile image (e.g., using an Intent for gallery/camera)
        });
    }

    private void loadUserProfileData() {
        // In a real application, you'd fetch currentUserName and currentUserEmail
        // from your backend or local storage.
        // For Firebase, you might get it from mAuth.getCurrentUser().getDisplayName() or getEmail()
        if (mAuth.getCurrentUser() != null) {
            currentUserName = mAuth.getCurrentUser().getDisplayName() != null ? mAuth.getCurrentUser().getDisplayName() : "No Name";
            currentUserEmail = mAuth.getCurrentUser().getEmail() != null ? mAuth.getCurrentUser().getEmail() : "No Email";
        }

        textViewUserName.setText(currentUserName);
        textViewUserEmail.setText(currentUserEmail);
    }

    private void showEditProfileDialog() {
        EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance(currentUserName, currentUserEmail);
        dialog.show(getSupportFragmentManager(), EditProfileDialogFragment.TAG);
    }

    @Override
    public void onSaveClick(String newUserName) {
        currentUserName = newUserName;
        textViewUserName.setText(currentUserName);
        // TODO: In a real app, update this newUserName in Firebase (e.g., user.updateProfile)
        // or your backend, and then refresh the UI on success.
        Toast.makeText(this, "User Name updated to: " + newUserName, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a confirmation dialog before signing out.
     */
    private void showSignOutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                // Positive button (Yes)
                .setPositiveButton("Yes", (dialog, which) -> performFirebaseSignOut())
                // Negative button (No)
                .setNegativeButton("No", (dialog, which) -> {
                    // User cancelled the sign-out, do nothing or show a message
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Optional: add an alert icon
                .show();
    }

    /**
     * Performs the Firebase sign-out operation and navigates to the LoginActivity.
     */
    private void performFirebaseSignOut() {
        if (mAuth != null) {
            mAuth.signOut(); // Perform Firebase sign out

            // Clear any local session data if you store it separately (e.g., SharedPreferences)
            // Example: getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();

            Toast.makeText(ProfileActivity.this, "Signed out successfully.", Toast.LENGTH_SHORT).show();

            // Navigate to LoginActivity and clear the back stack
            Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
            // These flags ensure the user cannot go back to ProfileActivity after signing out
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Finish ProfileActivity so it's removed from the back stack
        } else {
            Toast.makeText(ProfileActivity.this, "Error: Firebase Auth not initialized.", Toast.LENGTH_SHORT).show();
        }
    }
}