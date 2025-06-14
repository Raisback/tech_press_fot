package com.fotuoc.techpress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast; // For simple feedback
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class EditProfileDialogFragment extends DialogFragment {

    public static final String TAG = "EditProfileDialog";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_USEREMAIL = "useremail";

    private TextInputEditText editTextUserName;
    private TextInputEditText editTextEmail;

    // Interface to communicate back to the hosting Activity
    // Now includes the newEmail parameter
    public interface EditProfileDialogListener {
        void onSaveClick(String newUserName, String newEmail);
    }

    private EditProfileDialogListener listener;

    // Factory method to create a new instance of the dialog
    public static EditProfileDialogFragment newInstance(String currentUserName, String currentUserEmail) {
        EditProfileDialogFragment fragment = new EditProfileDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, currentUserName);
        args.putString(ARG_USEREMAIL, currentUserEmail);
        fragment.setArguments(args);
        return fragment;
    }

    // Attach the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Ensure the hosting activity implements the listener interface
            listener = (EditProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement EditProfileDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the custom layout for the dialog
        return inflater.inflate(R.layout.dialog_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Get arguments passed to the dialog (current username and email)
        Bundle args = getArguments();
        if (args != null) {
            String currentUserName = args.getString(ARG_USERNAME);
            String currentUserEmail = args.getString(ARG_USEREMAIL);

            // Set the current values to the EditText fields
            editTextUserName.setText(currentUserName);
            editTextEmail.setText(currentUserEmail);
        }

        // Set OnClickListener for the Cancel button to dismiss the dialog
        btnCancel.setOnClickListener(v -> dismiss());

        // Set OnClickListener for the Save button to validate and save changes
        btnSave.setOnClickListener(v -> {
            String newUserName = editTextUserName.getText().toString().trim();
            String newEmail = editTextEmail.getText().toString().trim(); // Get new email value

            // Input validation for User Name
            if (newUserName.isEmpty()) {
                Toast.makeText(getContext(), "User Name cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Input validation for Email
            if (newEmail.isEmpty()) {
                Toast.makeText(getContext(), "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(newEmail)) {
                Toast.makeText(getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Communicate the new user name and email back to the activity via the listener
            if (listener != null) {
                listener.onSaveClick(newUserName, newEmail);
            }
            dismiss(); // Close the dialog after successful save
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // This ensures the dialog respects Material Design guidelines
        return new Dialog(requireContext(), com.google.android.material.R.style.Theme_MaterialComponents_Dialog);
    }

    // Optional: Set dialog style (e.g., to be full-width)
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    /**
     * Helper method to validate email format using a regular expression.
     * @param email The email string to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        // Simple regex for email validation (can be more complex if needed)
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}