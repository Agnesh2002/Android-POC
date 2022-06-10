package com.example.myapplication.ui.dashboard;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ui.profile.ProfileFragment;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    CardView weather,find_n_connect,profile,live_stream;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        weather = view.findViewById(R.id.card_weather);
        profile = view.findViewById(R.id.card_profile);
        find_n_connect = view.findViewById(R.id.card_bluetooth);
        live_stream = view.findViewById(R.id.card_stream);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_side_menu);
                navController.navigate(R.id.nav_profile);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_side_menu);
                navController.navigate(R.id.nav_weather);
            }
        });

        find_n_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_side_menu);
                navController.navigate(R.id.nav_ble);
            }
        });

        live_stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_side_menu);
                navController.navigate(R.id.nav_live_stream);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        // TODO: Use the ViewModel
    }

}