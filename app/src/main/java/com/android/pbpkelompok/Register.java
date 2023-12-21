package com.android.pbpkelompok;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Register extends BottomSheetDialogFragment {

    private Spinner warungSpinner;
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
        warungSpinner = view.findViewById(R.id.warungID);
        Button daftar = view.findViewById(R.id.btnRegister);

        db = new DataBaseHelperLogin(getActivity());

        // Pilihan roleSpinner
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"Petugas Dapur"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        // Pilihan warungSpinner
        ArrayAdapter<String> warungAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"Warung A", "Warung B", "Warung C", "Warung D", "Warung E"});
        warungAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warungSpinner.setAdapter(warungAdapter);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inUsername = username.getText().toString();
                String inPassword = password.getText().toString();
                String inRePassword = repassword.getText().toString();
                String inFullName = fullName.getText().toString();
                String inRoleID = db.getRoleIdByName(roleSpinner.getSelectedItem().toString());
                String inStatus = "aktif";

                // Validasi input
                if (inUsername.isEmpty() || inPassword.isEmpty() || inRePassword.isEmpty() || inFullName.isEmpty()) {
                    Toast.makeText(getActivity(), "Semua inputan harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!inRePassword.equals(inPassword)) {
                    repassword.setError("Password Tidak Sama");
                    return;
                }

                // Validasi username sudah ada atau belum
                if (checkUsername(inUsername)) {
                    Toast.makeText(getActivity(), "Username sudah digunakan", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Updated method call to include Full Name, Role ID, and Status parameters
                Boolean daftarSuccess = daftar(inUsername, inPassword, inFullName, inRoleID, inStatus, null);
                if (daftarSuccess) {
                    Toast.makeText(getActivity(), "Daftar Berhasil", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Daftar Gagal", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });
    }

    private Boolean daftar(String username, String password, String namaPengguna, String idRole, String status, byte[] foto) {

        // Get formatted date
        String formattedDate = getFormattedDate();

        // Get selected warungID from Spinner
        String selectedWarung = db.getWarungIdByName(warungSpinner.getSelectedItem().toString());

        // Combine warungID, formatted date, and "X" to create the initial idpengguna
        String initialIdpengguna = selectedWarung + formattedDate + "X";

        // Get the number of employees with idpengguna starting with initialIdpengguna
        int employeeCount = db.getEmployeeCountWithPrefix(initialIdpengguna);

        // Create the final idpengguna by appending "X" and the employee count
        String finalIdpengguna = initialIdpengguna + String.format("%02d", employeeCount + 1);

        // Updated method call to include idpengguna parameter
        return db.simpanUser(finalIdpengguna, username, password, namaPengguna, idRole, status, foto);
    }

    private String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private boolean checkUsername(String username) {
        return db.checkUsername(username);
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
