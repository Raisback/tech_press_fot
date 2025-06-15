package com.fotuoc.techpress;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Added for logging
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// Added for @NonNull annotation
import androidx.appcompat.app.AppCompatActivity;

// Added for OnCompleteListener
// Added for Task
// Might be needed for re-auth
// Might be needed for re-auth
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest; // Added for profile update

public class ProfileActivity extends AppCompatActivity implements EditProfileDialogFragment.EditProfileDialogListener {

    private static final String TAG = "ProfileActivity"; // Added TAG for logging

    private TextView textViewUserName;
    private TextView textViewUserEmail;

    // Firebase Auth instance
    private FirebaseAuth mAuth;

    // Example user data (in a real app, this would come from a database/API)
    // These will now be loaded from Firebase Auth
    private String currentUserName;
    private String currentUserEmail;

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
        //TextView btnEditProfile = findViewById(R.id.btnEditProfile);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button btnAbout = findViewById(R.id.btnDev);

        // Populate User Data
        loadUserProfileData();

        // --- Set up Click Listeners ---
        btnEdit.setOnClickListener(v -> showEditProfileDialog());

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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            currentUserName = user.getDisplayName() != null ? user.getDisplayName() : "No Name Set";
            currentUserEmail = user.getEmail() != null ? user.getEmail() : "No Email Set";
        } else {
            // Handle case where user is not logged in (e.g., redirect to login)
            currentUserName = "Guest";
            currentUserEmail = "N/A";
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            // Consider redirecting to AuthActivity
        }

        textViewUserName.setText(currentUserName);
        textViewUserEmail.setText(currentUserEmail);
    }

    private void showEditProfileDialog() {
        EditProfileDialogFragment dialog = EditProfileDialogFragment.newInstance(currentUserName, currentUserEmail);
        dialog.show(getSupportFragmentManager(), EditProfileDialogFragment.TAG);
    }

    /**
     * Callback from EditProfileDialogFragment when save button is clicked.
     * This is where Firebase Auth user profile updates are performed.
     */
    @Override
    public void onSaveClick(String newUserName, String newEmail) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            boolean emailChanged = !newEmail.equals(currentUserEmail);
            boolean nameChanged = !newUserName.equals(currentUserName);

            if (emailChanged) {
                // --- Update User Email ---
                user.updateEmail(newEmail)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                                currentUserEmail = newEmail; // Update local variable
                                textViewUserEmail.setText(newEmail); // Update UI
                                Toast.makeText(ProfileActivity.this, "Email updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Error updating email.", task.getException());
                                // Most common error: FirebaseTooManyRequestsException (if not recently signed in)
                                // or FirebaseAuthInvalidCredentialsException (invalid format)
                                // If it's RECENT_LOGIN_REQUIRED, you need to re-authenticate the user.
                                Toast.makeText(ProfileActivity.this,
                                        "Failed to update email: " + task.getException().getMessage() +
                                                "\n(Might require recent login)",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }

            if (nameChanged) {
                // --- Update User Display Name ---
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newUserName)
                        // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg")) // Optional: for profile image
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated (display name).");
                                currentUserName = newUserName; // Update local variable
                                textViewUserName.setText(newUserName); // Update UI
                                Toast.makeText(ProfileActivity.this, "Display name updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Error updating display name.", task.getException());
                                Toast.makeText(ProfileActivity.this,
                                        "Failed to update display name: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }

            if (!emailChanged && !nameChanged) {
                Toast.makeText(this, "No changes detected.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "User not logged in. Cannot update profile.", Toast.LENGTH_SHORT).show();
            // Optionally redirect to login
        }
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