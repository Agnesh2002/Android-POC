package com.example.myapplication.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileAcitivity extends AppCompatActivity {

    EditText username,fullname,dob,email_id,phone;
    Button save,edit;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    String image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        username = findViewById(R.id.et_edit_profile_user_name);
        fullname = findViewById(R.id.et_edit_profile_full_name);
        dob = findViewById(R.id.et_edit_profile_dob);
        email_id = findViewById(R.id.et_edit_profile_email_id);
        phone = findViewById(R.id.et_edit_profile_phone);
        save = findViewById(R.id.btn_save_edit_profile);
        edit = findViewById(R.id.buttonn_edit_edit_profile);

        username.setEnabled(false);
        fullname.setEnabled(false);
        dob.setEnabled(false);
        email_id.setEnabled(false);
        phone.setEnabled(false);

        save.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        username.setText(firebaseUser.getDisplayName());
        email_id.setText(firebaseUser.getEmail());

        firestore.collection("USERS").document(email_id.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot = task.getResult();
                            ProfileData obj = snapshot.toObject(ProfileData.class);

                            fullname.setText(obj.getFullname());
                            dob.setText(obj.getDob());
                            phone.setText(obj.getPhone());
                            image_uri = obj.getImage_uri();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username.setEnabled(true);
                fullname.setEnabled(true);
                dob.setEnabled(true);
                phone.setEnabled(true);

                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_username = username.getText().toString().trim();
                String s_fullname = fullname.getText().toString().trim();
                String s_dob = dob.getText().toString().trim();
                String s_phone = phone.getText().toString().trim();

                ProfileData obj = new ProfileData(s_fullname,s_dob,s_phone,image_uri);

                if(!s_username.equals(firebaseUser.getDisplayName()))
                {
                    UserProfileChangeRequest profileChangeRequest;
                    profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(s_username)
                            .build();
                    firebaseUser.updateProfile(profileChangeRequest);

                }

                firestore.collection("USERS").document(email_id.getText().toString())
                        .set(obj)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(EditProfileAcitivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(EditProfileAcitivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfileAcitivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                username.setEnabled(false);
                fullname.setEnabled(false);
                dob.setEnabled(false);
                email_id.setEnabled(false);
                phone.setEnabled(false);

                save.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);

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

