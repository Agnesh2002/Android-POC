package com.example.myapplication.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    TextView tv_uname,tv_fname,tv_dob,tv_email,tv_phone,tv_go_to_edit_profile;
    ImageView profile_pic;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    FirebaseFirestore firestore;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_uname = view.findViewById(R.id.tv_profile_welcome_text);
        tv_fname = view.findViewById(R.id.tv_profile_full_name);
        tv_dob = view.findViewById(R.id.tv_profile_dob);
        tv_email = view.findViewById(R.id.tv_profile_email_id);
        tv_phone = view.findViewById(R.id.tv_profile_phone);
        tv_go_to_edit_profile = view.findViewById(R.id.tv_go_to_edit_profile);
        profile_pic = view.findViewById(R.id.profile_profile_pic);
        progressBar = view.findViewById(R.id.progressBar_profile);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();


        tv_uname.setText(firebaseUser.getDisplayName());
        tv_email.setText(firebaseUser.getEmail());

        firestore.collection("USERS").document(tv_email.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String img_uri = documentSnapshot.getString("image_uri");

                        Glide.with(getContext())
                                .load(img_uri)
                                .fitCenter()
                                .placeholder(R.drawable.ic_baseline_person_pin_24)
                                .into(profile_pic);
                    }
                });


        firestore.collection("USERS").document(tv_email.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot = task.getResult();
                            ProfileData obj = snapshot.toObject(ProfileData.class);

                            tv_fname.setText(obj.getFullname());
                            tv_dob.setText(obj.getDob());
                            tv_phone.setText(obj.getPhone());

                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        else
                        {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        tv_go_to_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileAcitivity.class));
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfilePicture.class));
            }
        });


        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//        // TODO: Use the ViewModel
//    }

}