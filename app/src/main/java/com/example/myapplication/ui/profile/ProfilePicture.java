package com.example.myapplication.ui.profile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilePicture extends AppCompatActivity {

    Button choose,upload;
    ImageView picture;
    Uri image_uri;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        choose = findViewById(R.id.btn_choose_image);
        upload = findViewById(R.id.btn_upload_image);
        picture = findViewById(R.id.picture);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = auth.getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Picture");

        CollectionReference collectionReference = firestore.collection("USERS");

        collectionReference.document(firebaseUser.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String img_uri = documentSnapshot.getString("image_uri");

                        Glide.with(ProfilePicture.this)
                                .load(img_uri)
                                .fitCenter()
                                .placeholder(R.drawable.ic_baseline_person_pin_24)
                                .into(picture);
                    }
                });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(ProfilePicture.this);
                progressDialog.setMessage("Uploading please wait...\nThis depends upon your network speed");
                progressDialog.show();

                if(image_uri != null)
                {
                    StorageReference reference = firebaseStorage.getReference().child("IMAGES/"+firebaseUser.getEmail());

                    reference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        image_uri = uri;

                                        collectionReference.document(firebaseUser.getEmail())
                                                .update("image_uri",image_uri.toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(ProfilePicture.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(ProfilePicture.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                });

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(ProfilePicture.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result!=null)
            {
                picture.setImageURI(result);
                image_uri = result;
                upload.setVisibility(View.VISIBLE);
                Toast.makeText(ProfilePicture.this, "An image has been selected", Toast.LENGTH_SHORT).show();
            }
            else
            {
                upload.setVisibility(View.INVISIBLE);
            }
        }
    });

}