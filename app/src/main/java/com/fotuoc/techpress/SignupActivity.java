package com.fotuoc.techpress;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest; // Import for updating user profile

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private FirebaseAuth mAuth;

    // Declare the new username EditText
    private TextInputEditText usernameEditText, emailEditText, passwordEditText, confirmPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // Ensure this matches your layout file name,
        // it was 'signup' but commonly 'activity_signup'

        mAuth = FirebaseAuth.getInstance();

        // Initialize the new username EditText
        usernameEditText = findViewById(R.id.signup_username_edit_text);
        emailEditText = findViewById(R.id.signup_email_edit_text);
        passwordEditText = findViewById(R.id.signup_password_edit_text);
        confirmPsd = findViewById(R.id.signup_reenter_password_edit_text);
        TextView tologin = findViewById(R.id.tologin);

        MaterialButton signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> registerUser());

        tologin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, AuthActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        // Get text from all fields, including the new username field
        String username = Objects.requireNonNull(usernameEditText.getText()).toString().trim();
        String email = Objects.requireNonNull(Objects.requireNonNull(emailEditText.getText())).toString().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPsd.getText()).toString().trim();

        // --- Input Validation ---

        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("User name is required.");
            usernameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required.");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters.");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPsd.setError("Please re-enter your password.");
            confirmPsd.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPsd.setError("Passwords do not match.");
            confirmPsd.requestFocus();
            return;
        }

        // --- Firebase User Creation ---
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User creation success
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // --- Update User Profile with Display Name ---
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username) // Set the display name
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            Log.d(TAG, "User profile updated successfully with display name.");
                                            Toast.makeText(SignupActivity.this, "Registration successful and profile updated.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.w(TAG, "Failed to update user profile with display name.", profileTask.getException());
                                            Toast.makeText(SignupActivity.this, "Registration successful but profile update failed: " + Objects.requireNonNull(profileTask.getException()).getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        // Regardless of profile update success, proceed to the main activity
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish(); // Finish SignupActivity
                                    });
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration successful. Could not get user to update profile.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish(); // Finish SignupActivity
                        }

                    } else {
                        // If sign in fails
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}