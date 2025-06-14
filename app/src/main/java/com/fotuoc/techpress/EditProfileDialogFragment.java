package com.fotuoc.techpress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast; // For simple feedback


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
    private Button btnCancel;
    private Button btnSave;

    // Interface to communicate back to the hosting Activity
    public interface EditProfileDialogListener {
        void onSaveClick(String newUserName);
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
            listener = (EditProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditProfileDialogListener");
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

        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);

        // Get arguments passed to the dialog
        Bundle args = getArguments();
        if (args != null) {
            String currentUserName = args.getString(ARG_USERNAME);
            String currentUserEmail = args.getString(ARG_USEREMAIL);

            editTextUserName.setText(currentUserName);
            editTextEmail.setText(currentUserEmail);
        }

        btnCancel.setOnClickListener(v -> dismiss()); // Close the dialog

        btnSave.setOnClickListener(v -> {
            String newUserName = editTextUserName.getText().toString().trim();

            if (newUserName.isEmpty()) {
                Toast.makeText(getContext(), "User Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Communicate the new user name back to the activity
            if (listener != null) {
                listener.onSaveClick(newUserName);
            }
            dismiss(); // Close the dialog after saving
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use a MaterialComponents dialog theme for consistent styling
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
}