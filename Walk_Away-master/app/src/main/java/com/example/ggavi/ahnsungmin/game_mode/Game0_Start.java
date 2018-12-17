package com.example.ggavi.ahnsungmin.game_mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ggavi.ahnsungmin.R;


public class Game0_Start extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game0_activity_start);

        // ActionBar Hide!
        getSupportActionBar().hide();

        Buttons_Working();
    }

    private void Buttons_Working()
    {
        Button New_Game=findViewById(R.id.New_Game_Button_ID);
        Button Load_Game=findViewById(R.id.Load_Game_Button_ID);


        // 새로시작 버튼
        New_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                SharedPreferences Data_Box= getApplicationContext().getSharedPreferences("Data_Box",MODE_PRIVATE);

                SharedPreferences.Editor Editor=Data_Box.edit();
                Editor.putInt("Day",0);
                Editor.putInt("Family_One_Hungry",100);
                Editor.putInt("Family_One_Thirst",100);
                Editor.putInt("Family_One_Hp",100);
                Editor.putInt("Family_Two_Hungry",100);
                Editor.putInt("Family_Two_Thirst",100);
                Editor.putInt("Family_Two_Hp",100);
                Editor.putInt("Food",10);
                Editor.putInt("Water",10);
                Editor.putInt("Global_Damage",-1);
                Editor.putInt("Family_One_Damage",-1);
                Editor.putInt("Family_Two_Damage",-1);
                Editor.putBoolean("Family_One_Not_Died", true);
                Editor.putBoolean("Family_Two_Not_Died", true);

                Editor.commit();
                Intent Launch_Activity=new Intent(Game0_Start.this,Game1_Day.class);
                startActivity(Launch_Activity);
            }
        });



        // 불러오기 버튼 : 그냥 이동
        Load_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent Launch_Activity=new Intent(Game0_Start.this,Game1_Day.class);
                startActivity(Launch_Activity);
            }
        });
    }
}