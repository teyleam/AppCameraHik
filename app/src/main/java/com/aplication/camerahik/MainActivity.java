package com.aplication.camerahik;


import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "bbb.m4v";

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;
    private VLCVideoLayout videoLayout;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoLayout = findViewById(R.id.videoLayout);
        btnDownload = findViewById(R.id.downloadUrl);

        mLibVLC = new LibVLC(this, new ArrayList<String>(){{
            add("--no-drop-late-frames");
            add("--file-caching=2000");
            add("--no-skip-frames");
            add("--rtsp-tcp");
            add("-vvv");
        }});

        mMediaPlayer = new MediaPlayer(mLibVLC);
//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }




    @Override
    protected void onStart() {
        super.onStart();

        mMediaPlayer.attachViews(videoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);

        String name = "admin";
        String password = "abcd@111";
        String cameraUrl = "192.168.1.46:554";
        String stream = "Streaming/Channels/101";
        String rtspUrl = "rtsp://" + name + ":" + password + "@" + cameraUrl+"/"+stream;
      //  String httpUrl = "https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4";
        Uri uri = Uri.parse(rtspUrl); // ..whatever you want url...or even file fromm asset
            final Media media = new Media(mLibVLC, uri);
            media.setHWDecoderEnabled(true, false);
            media.addOption(":network-caching=150");
            media.addOption(":clock-jitter=0");
            media.addOption(":clock-synchro=0");
            mMediaPlayer.setMedia(media);
            mMediaPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }
}