package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    EditText email,password;
    Button login;
    TextView go_to_register,forgot_password;
    CheckBox checkBox;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_login_email_id);
        password = findViewById(R.id.et_login_password);
        login = findViewById(R.id.btn_login_login);
        go_to_register = findViewById(R.id.tv_login_go_to_register);
        forgot_password = findViewById(R.id.tv_login_reset_password);
        checkBox = findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("REMEMBER",MODE_PRIVATE);

        String stored_email = sharedPreferences.getString("EMAIL",null);
        String stored_uname = sharedPreferences.getString("UNAME",null);
        String status = sharedPreferences.getString("STATUS","unchecked");

        if(status.equals("checked"))
        {
            Toast.makeText(getApplicationContext(), "Hi, "+stored_uname+".", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),SideMenuActivity.class);
            i.putExtra("UNAME",stored_uname);
            i.putExtra("EMAIL",stored_email);
            startActivity(i);
            LoginActivity.this.finish();
        }
        else
        {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String s_email = email.getText().toString().trim();
                String s_password = password.getText().toString().trim();

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

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(s_email,s_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    firebaseUser = auth.getCurrentUser();
                                    String uname = firebaseUser.getDisplayName();

                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putString("EMAIL",s_email);
                                    editor.putString("UNAME",uname);
                                    editor.putString("STATUS","checked");
                                    editor.commit();

                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, "Welcome, "+uname+".", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(),SideMenuActivity.class);
                                    i.putExtra("UNAME",uname);
                                    i.putExtra("EMAIL",firebaseUser.getEmail());
                                    startActivity(i);
                                    LoginActivity.this.finish();

                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_email = email.getText().toString().trim();

                if(s_email.isEmpty())
                {
                    email.setError("Please enter the registered e-mail id here");
                    email.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(s_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginActivity.this,
                                            "A password reset link has been sent to your email. \n After resetting password, " +
                                                    "you can login with the new password", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });



    }
}