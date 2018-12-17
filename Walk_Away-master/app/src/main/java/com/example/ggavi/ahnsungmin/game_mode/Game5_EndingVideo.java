package com.example.ggavi.ahnsungmin.game_mode;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ggavi.ahnsungmin.R;

public class Game5_EndingVideo extends Activity {

    // 뒤로가기 버튼 변수
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game5_activity_video_ending);

        backPressCloseHandler = new BackPressCloseHandler(this);  // 뒤로가기 버튼용 변수
        VideoView vv = (VideoView) findViewById(R.id.vv);

        // http://www ~~~ possible
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.game2good;
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
    }






    // 뒤로가기 버튼 누를시 동작
    @Override
    public void onBackPressed()
    {
        backPressCloseHandler.onBackPressed();
    }

    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;
        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
                finish();
                activity.finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
                toast.cancel();
            }
        }

        public void showGuide()
        {
            toast = Toast.makeText(activity, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}