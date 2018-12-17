package com.example.ggavi.ahnsungmin.game_mode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ggavi.ahnsungmin.R;

import java.util.Random;


public class Game4_Run extends AppCompatActivity implements SensorEventListener {

    //private BackPressCloseHandler backPressCloseHandler;  // 뒤로가기 버튼용 변수
    private int Collected_Food, Collected_Water;
    private int View_Collected_Food, View_Collected_Water;

    int Temp_Step_Taken;
    TextView Steps_View, Collected_Food_View, Collected_Water_View;
    SharedPreferences Data_Box;


    // 만보기 변수선언 투척
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    //////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game4_activity_run);

        // ActionBar Hide!
        getSupportActionBar().hide();

        //backPressCloseHandler = new BackPressCloseHandler(this);  // 뒤로가기 버튼용 변수

        Temp_Step_Taken = Collected_Food = Collected_Water = 0;
        View_Collected_Food = View_Collected_Water = 0;

        Data_Box = getApplicationContext().getSharedPreferences("Data_Box", MODE_PRIVATE);
        Image_Setting();
        Initiating_Views();


        //// 만보기 기능 투척 ////
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Steps_View = (TextView) findViewById(R.id.Steps_Taken_View_Id);
        Steps_View.setText("" + Temp_Step_Taken);
        /////////////////////////
    }


    //// 만보기 함수 투척 ////
    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 120) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Temp_Step_Taken++;

                    if (Temp_Step_Taken % 100 == 0) {
                        Collected_Food = Collected_Food + 2;
                        Collected_Water++;

                    } else {
                        Collected_Food = 0;
                        Collected_Water = 0;
                    }
                    Put_And_Update();
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // ↑ 여기까지가 만보기 관련 함수
    /////////////////////////////////////////////////////////////////////




    private void Initiating_Views() {
        Chronometer Chronometer_View = findViewById(R.id.Chronometer_View_Id);
        Steps_View = findViewById(R.id.Steps_Taken_View_Id);
        Collected_Food_View = findViewById(R.id.Collected_Food_View_Id);
        Collected_Water_View = findViewById(R.id.Collected_Water_View_Id);
        Button Click_Button = findViewById(R.id.Run_Button_Id);
        Button End_Walk = findViewById(R.id.Stop_walk_Button_Id);
        Put_And_Update();

        Chronometer_View.setBase(SystemClock.elapsedRealtime());
        Chronometer_View.start();


        // 마우스 클릭
        Click_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temp_Step_Taken++;
                if (Temp_Step_Taken % 100 == 0) {
                    Collected_Food = Collected_Food + 2;
                    Collected_Water++;

                } else {
                    Collected_Food = 0;
                    Collected_Water = 0;
                }
                Put_And_Update();

            }
        });


        // 걷기 종료를 누른 경우
        End_Walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Temp_Day = Data_Box.getInt("Day", 0);
                Temp_Day++;  // 다음 날짜로 ++


                // 1-1. 배고픔, 목마름 수치 받아오기
                int Temp_One_Hungry = Data_Box.getInt("Family_One_Hungry", 100);
                int Temp_Two_Hungry = Data_Box.getInt("Family_Two_Hungry", 100);
                int Temp_One_Thirst = Data_Box.getInt("Family_One_Thirst", 100);
                int Temp_Two_Thirst = Data_Box.getInt("Family_Two_Thirst", 100);

                // 1-2. 데미지 수치 받아오기
                int Temp_Global_Damage = Data_Box.getInt("Global_Damage", -1);
                int Temp_Family_One_Damage = Data_Box.getInt("Family_One_Damage", -1);
                int Temp_Family_Two_Damage = Data_Box.getInt("Family_Two_Damage", -1);
                int Temp_One_Hp = Data_Box.getInt("Family_One_Hp", 100);
                int Temp_Two_Hp = Data_Box.getInt("Family_Two_Hp", 100);


                // 21. 배고픔, 목마름 마이너스 시키기 위해 랜덤 함수 호출
                Random r = new Random();
                int Random_One_Value1 = r.nextInt(20 - 10) + 10;  // 배고프면 한 5일 버티려나
                int Random_One_Value2 = r.nextInt(33 - 10) + 10;  // 목마르면 한 3일 버티려나
                int Random_Two_Value1 = r.nextInt(20 - 10) + 10;
                int Random_Two_Value2 = r.nextInt(33 - 10) + 10;


                // 3-1. 배고픔, 목마름 마이너스 시키기
                Temp_One_Hungry = Temp_One_Hungry - Random_One_Value1;
                Temp_Two_Hungry = Temp_Two_Hungry - Random_Two_Value1;
                Temp_One_Thirst = Temp_One_Thirst - Random_One_Value2;
                Temp_Two_Thirst = Temp_Two_Thirst - Random_Two_Value2;

                // 3-2. 데미지 마이너스
                Temp_Global_Damage = Temp_Global_Damage - Temp_Day;
                Temp_Family_One_Damage = Temp_Family_One_Damage - Temp_Day;
                Temp_Family_Two_Damage = Temp_Family_Two_Damage - Temp_Day;
                Temp_One_Hp = Temp_One_Hp + Temp_Family_One_Damage;
                Temp_Two_Hp = Temp_Two_Hp + Temp_Family_Two_Damage;


                // 4. Day +1, 갱신된 배고픔과 목마름, 데미지 숫자 삽입!!
                SharedPreferences.Editor editor = Data_Box.edit();
                editor.putInt("Day", Temp_Day);
                editor.putInt("Family_One_Hungry", Temp_One_Hungry);
                editor.putInt("Family_Two_Hungry", Temp_Two_Hungry);
                editor.putInt("Family_One_Thirst", Temp_One_Thirst);
                editor.putInt("Family_Two_Thirst", Temp_Two_Thirst);
                editor.putInt("Family_One_Hp", Temp_One_Hp);
                editor.putInt("Family_Two_Hp", Temp_Two_Hp);
                editor.putInt("Global_Damage", Temp_Global_Damage);
                editor.putInt("Family_One_Damage", Temp_Family_One_Damage);
                editor.putInt("Family_Two_Damage", Temp_Family_Two_Damage);

                editor.commit();


                // 화면 전환
                startActivity(new Intent(Game4_Run.this, Game1_Day.class));
                finish();
            }
        });
    }


    private void Put_And_Update() {
        View_Collected_Food = View_Collected_Food + Collected_Food;
        View_Collected_Water = View_Collected_Water + Collected_Water;

        Steps_View.setText(Temp_Step_Taken + "");
        Collected_Food_View.setText(View_Collected_Food + "");
        Collected_Water_View.setText(View_Collected_Water + "");

        int Food = Data_Box.getInt("Food", 1);
        int Water = Data_Box.getInt("Water", 1);

        Food = Food + Collected_Food;
        Water = Water + Collected_Water;

        SharedPreferences.Editor editor = Data_Box.edit();
        editor.putInt("Food", Food);
        editor.putInt("Water", Water);
        editor.commit();
    }

    private void Image_Setting() {
        int Day = Data_Box.getInt("Day", 0);
        TypedArray Images_Array = getResources().obtainTypedArray(R.array.Images_Data);
        String Image_Description[] = getResources().getStringArray(R.array.Image_Description);

        ImageView Status_Image = findViewById(R.id.WalkImage_View_Id);
        TextView Status_Image_Description = findViewById(R.id.WalkImage_Des_View_Id);
        Status_Image.setImageResource(Images_Array.getResourceId(Day, -1));
        Status_Image_Description.setText(Image_Description[Day]);
        Images_Array.recycle();
    }

/*
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        startActivity(new Intent(Game4_Run.this, Game2_Main.class));
        finish();
    }
*/

    // 뒤로가기 입력을 하는 경우.
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 다이얼로그의 긍정 이벤트일 경우 종료한다.
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }


}

/*
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
                startActivity(new Intent(Game4_Run.this, Game2_Main.class));
                finish();
                toast.cancel();
            }
        }

        public void showGuide()
        {
            toast = Toast.makeText(activity, "뒤로가기 버튼을 한번 더 누르시면 이동됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
*/





