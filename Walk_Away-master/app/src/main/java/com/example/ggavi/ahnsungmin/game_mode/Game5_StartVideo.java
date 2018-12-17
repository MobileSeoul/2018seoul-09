package com.example.ggavi.ahnsungmin.game_mode;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ggavi.ahnsungmin.R;

public class Game5_StartVideo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game5_activity_video_start);
        VideoView vv = (VideoView) findViewById(R.id.vv);

        // http://www ~~~ possible
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.game3start;
        Uri uri = Uri.parse(uriPath);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();


        // Event
        vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        Toast.makeText(getApplicationContext(), "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_LONG).show();
                        break;

                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        // Progress Diaglog 출력(Print)
                        Toast.makeText(getApplicationContext(), "MEDIA_INFO_BUFFERING_START", Toast.LENGTH_LONG).show();
                        break;

                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        // Progress Dialog 삭제(Delete)
                        Toast.makeText(getApplicationContext(), "MEDIA_INFO_BUFFERING_END", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });


        // 마우스 클릭
        Button SkipButton = findViewById(R.id.SkipButton_Id);
        SkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}