package com.example.hanut.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hanut.R;
import com.example.hanut.models.User;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText textInputUserName;
    TextInputEditText textInputPhone;
    Button btnRegister;
    AuthProvider mAuthProvider;
    UserProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);


        // Así instanciamos los objetos
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage(R.string.wait).build();

        textInputUserName = findViewById(R.id.textInputUser);
        btnRegister = findViewById(R.id.btnRegister);
        textInputPhone = findViewById(R.id.textInputPhone);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    } // On create


    private void register(){
        String userName = textInputUserName.getText().toString();
        String phone = textInputPhone.getText().toString();
        if(!userName.isEmpty() && !phone.isEmpty()){
            update(userName, phone);
        }else{
            Toast.makeText(this, "Type info", Toast.LENGTH_LONG).show();
        }
    }

    // Método para crear el usuario
    private void update(final String userName, final String phone){
        String id = mAuthProvider.getUid();
        User user = new User();
        user.setUserName(userName);
        user.setId(id);
        user.setPhone(phone);
        user.setTimestamp(new Date().getTime());

        mDialog.show();
        // Update por get para que no sobreescriba los valores
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}