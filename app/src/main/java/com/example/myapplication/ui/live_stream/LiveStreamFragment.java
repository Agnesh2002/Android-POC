package com.example.myapplication.ui.live_stream;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class LiveStreamFragment extends Fragment {

    private LiveStreamViewModel mViewModel;
    public static LiveStreamFragment newInstance() {
        return new LiveStreamFragment();
    }

    Button play;
    String video_uri;
    PlayerView playerView;
    ExoPlayer simpleExoPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_live_stream, container, false);

        play = v.findViewById(R.id.btn_play_content);
        playerView = v.findViewById(R.id.video_view);
        simpleExoPlayer = new ExoPlayer.Builder(getContext()).build();

        video_uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(video_uri!=null)
                {

                    playerView.setPlayer(simpleExoPlayer);
                    MediaItem mediaItem = MediaItem.fromUri(video_uri);
                    simpleExoPlayer.addMediaItem(mediaItem);
                    simpleExoPlayer.prepare();
                    simpleExoPlayer.setPlayWhenReady(true);

                }
                else
                {
                    Toast.makeText(getContext(), "Video not available", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LiveStreamViewModel.class);
        // TODO: Use the ViewModel
    }

}