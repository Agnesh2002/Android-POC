package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.ui.profile.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {

    EditText username,email,password,confirm_password;
    Button register;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email_id);
        password = findViewById(R.id.et_password);
        confirm_password = findViewById(R.id.et_confirm_password);
        register = findViewById(R.id.btn_registration_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_username = username.getText().toString().trim();
                String s_email = email.getText().toString().trim();
                String s_password = password.getText().toString().trim();
                String s_confirm_password = confirm_password.getText().toString().trim();

                if(s_username.isEmpty())
                {
                    username.setError("This field is required");
                    username.requestFocus();
                    return;
                }
                if(s_email.isEmpty())
                {
                    email.setError("This field is required");
                    email.requestFocus();
                    return;
                }
                if(s_password.isEmpty())
                {
                    password.setError("This field is required");
                    password.requestFocus();
                    return;
                }
                if(s_confirm_password.isEmpty())
                {
                    confirm_password.setError("This field is required");
                    confirm_password.requestFocus();
                    return;
                }
                if(!s_password.equals(s_confirm_password))
                {
                    confirm_password.setError("Passwords does not match");
                    confirm_password.setText("");
                    confirm_password.requestFocus();
                    return;
                }

                auth.createUserWithEmailAndPassword(s_email,s_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseUser firebaseUser;
                                    firebaseUser = auth.getCurrentUser();

                                    UserProfileChangeRequest profileChangeRequest;
                                    profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(s_username)
                                            .build();
                                    firebaseUser.updateProfile(profileChangeRequest)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                ProfileData obj = new ProfileData("","","","");
                                                                firestore.collection("USERS").document(s_email).set(obj);
                                                                Toast.makeText(RegistrationActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(RegistrationActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                }
                                else
                                {
                                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}