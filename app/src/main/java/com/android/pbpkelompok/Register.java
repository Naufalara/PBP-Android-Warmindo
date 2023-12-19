package com.android.pbpkelompok;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Register extends BottomSheetDialogFragment {

    private DataBaseHelperLogin db;

    public static final String TAG = "Register";

    public static Register newInstance(){
        return new Register();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText username = view.findViewById(R.id.etUsername);
        EditText password = view.findViewById(R.id.etPassword);
        EditText repassword = view.findViewById(R.id.etRepeatPassword);
        EditText fullName = view.findViewById(R.id.etFullName); // Added EditText for Full Name
        Spinner roleSpinner = view.findViewById(R.id.roleID); // Changed to Spinner
        Button daftar = view.findViewById(R.id.btnRegister);

        db = new DataBaseHelperLogin(getActivity());

        // Set up the adapter and options for the roleSpinner
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"petugas dapur"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inUsername = username.getText().toString();
                String inPassword = password.getText().toString();
                String inRePassword = repassword.getText().toString();
                String inFullName = fullName.getText().toString(); // Get Full Name EditText value
                String inRoleID = roleSpinner.getSelectedItem().toString(); // Get selected role from Spinner
                String inStatus = "aktif"; // You can set a default value or remove this line if not needed

                if (!inRePassword.equals(inPassword)) {
                    repassword.setError("Password Tidak Sama");
                } else {
                    // Updated method call to include Full Name, Role ID, and Status parameters
                    Boolean daftarSuccess = db.simpanUser(inUsername, inPassword, inFullName, inRoleID, inStatus, null);
                    if (daftarSuccess) {
                        Toast.makeText(getActivity(), "Daftar Berhasil", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Daftar Gagal", Toast.LENGTH_LONG).show();
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
