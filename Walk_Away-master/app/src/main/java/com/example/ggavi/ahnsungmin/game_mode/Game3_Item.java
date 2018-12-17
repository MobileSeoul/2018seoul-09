package com.example.ggavi.ahnsungmin.game_mode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ggavi.ahnsungmin.R;

public class Game3_Item extends AppCompatActivity {

    private int Food, Water, Damage;
    private SharedPreferences Data_Box;
    private TextView Food_Amount_View, Water_Amount_View, Damage_View;
    int Reduced_Family_One_Damage, Reduced_Family_Two_Damage;
    AlertDialog.Builder New_Alert_Dialog;  // 다이어로그 띄우기 ('아이템을 구매할 수 없습니다!')


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game3_activity_item);

        // ActionBar Hide!
        getSupportActionBar().hide();

        New_Alert_Dialog = new AlertDialog.Builder(Game3_Item.this);  // 다이어로그 띄우기 ('아이템을 구매할 수 없습니다!')
        Reduced_Family_One_Damage = Reduced_Family_Two_Damage = 0;

        // ActionBar Hide!
        Data_Box = getApplicationContext().getSharedPreferences("Data_Box", MODE_PRIVATE);

        Initiating_Views();
    }


    private void Initiating_Views() {
        Food = Data_Box.getInt("Food", 1);
        Water = Data_Box.getInt("Water", 1);
        Damage = Data_Box.getInt("Global_Damage", -1);


        Food_Amount_View = findViewById(R.id.Food_Amount_View_GetItem_Id);
        Water_Amount_View = findViewById(R.id.Water_Amount_View_GetItem_Id);
        Damage_View = findViewById(R.id.Damage_Amount_View_Id);

        Get_And_Update();
        final Button Family_One_Buy = findViewById(R.id.Family_One_Buy_Button_Id);
        final Button Family_Two_Buy = findViewById(R.id.Family_Two_Buy_Button_Id);


        // 첫째놈이 아이템을 구매한 경우!
        Family_One_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Food >= 10 && Water >= 5) {
                    Use_Dialogue("아이템을 사용했습니다.");

                    Food = Food - 10;
                    Water = Water - 5;
                    Reduced_Family_One_Damage = Reduced_Family_One_Damage + 5;
                    Get_And_Update();

                    // 만약 버튼을 1번만 누를 수 있도록 만들고 싶을 경우:
                    // Family_One_Buy.setEnabled(false);
                } else if (Food < 10 || Water < 5) {
                    No_Use_Dialogue("아이템이 부족합니다.");
                } else {
                    No_Use_Dialogue("에러! 살 수 없습니다.");
                }
            }
        });


        // 둘째놈이 아이템을 구매한 경우!
        Family_Two_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Food >= 10 && Water >= 5) {
                    Use_Dialogue("아이템을 사용했습니다.");

                    Food = Food - 10;
                    Water = Water - 5;
                    Reduced_Family_Two_Damage = Reduced_Family_Two_Damage + 5;

                    Get_And_Update();
                    // 만약 버튼을 1번만 누를 수 있도록 만들고 싶을 경우:
                    // Family_Two_Buy.setEnabled(false);
                } else if (Food < 10 || Water < 5) {
                    No_Use_Dialogue("아이템이 부족합니다.");
                } else {
                    No_Use_Dialogue("에러! 살 수 없습니다.");
                }
            }
        });
    }

    private void Get_And_Update() {
        int Temp_Family_One_Damage, Temp_Family_Two_Damage;
        Temp_Family_One_Damage = Data_Box.getInt("Family_One_Damage", -1);
        Temp_Family_Two_Damage = Data_Box.getInt("Family_Two_Damage", -1);
        Temp_Family_One_Damage = Temp_Family_One_Damage + Reduced_Family_One_Damage;
        Temp_Family_Two_Damage = Temp_Family_Two_Damage + Reduced_Family_Two_Damage;
        Food_Amount_View.setText(Food + "");
        Water_Amount_View.setText(Water + "");
        Damage_View.setText(Damage+"");
        SharedPreferences.Editor Editor = Data_Box.edit();
        Editor.putInt("Food", Food);
        Editor.putInt("Water", Water);
        Editor.putInt("Family_One_Damage", Temp_Family_One_Damage);
        Editor.putInt("Family_Two_Damage", Temp_Family_Two_Damage);
        Editor.commit();
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        startActivity(new Intent(Game3_Item.this, Game2_Main.class));
        finish();
    }


    // 다이어로그 띄우기 ('아이템을 구매할 수 없습니다!')
    private void No_Use_Dialogue(String NoUse) {
        New_Alert_Dialog.setCancelable(false);
        New_Alert_Dialog.setTitle("아이템 제작 불가");
        New_Alert_Dialog.setMessage(NoUse);
        New_Alert_Dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    // 다이어로그 띄우기2 ('아이템을 구매할 수 없습니다!')
    private void Use_Dialogue(String NoUse) {
        New_Alert_Dialog.setCancelable(false);
        New_Alert_Dialog.setTitle("아이템 제작 완료");
        New_Alert_Dialog.setMessage(NoUse);
        New_Alert_Dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

}
