package com.example.ggavi.ahnsungmin;


// (24) 시작 화면을 만들기 위해 넣음 (실행시 나오는 로고 영상)
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class Splashscreen extends Activity {
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
    }

    private void StartAnimations()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l =(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1500)
                    {
                        sleep(100);
                        waited += 100;
                    }


                    Intent intent = new Intent(Splashscreen.this, FirstActivity.class);  // 스플래시 실행후 넘어가는 첫번째 화면!!
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();


/*

                    // 아래 코드 설명 : 만약 로그인이 안 되어 있으면 'FirstActivity'로 이동되고, 로그인이 되어 있으면 '로그인Activity'로 이동된다.
                    if(SavedSharedPreference.getId(Splashscreen.this).length() == 0)  //
                    {
                        Intent intent = new Intent(Splashscreen.this, FirstActivity.class);  // 스플래시 실행후 넘어가는 첫번째 화면!!
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Splashscreen.this.finish();
                    }

                    else
                    {
                        Intent intent = new Intent(Splashscreen.this, a_LoginMainActivity.class);  // 스플래시 실행후 넘어가는 첫번째 화면!!
                        intent.putExtra("userID",SavedSharedPreference.getId(Splashscreen.this));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Splashscreen.this.finish();
                    }

*/

                }

                catch (InterruptedException e)
                {
                    // do nothing
                }

                finally
                {
                    Splashscreen.this.finish();
                }
            }
        };
        splashTread.start();
    }

}