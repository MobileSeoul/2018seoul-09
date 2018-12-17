package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ggavi.ahnsungmin.R;

public class SettingActivity extends AppCompatActivity {

    private Button loggedInWeightSetting;
    private Button SMS_setting;
    private Button BlueTooth;
    private Button propicsetting;
    private String userId;
    Typeface font_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity4_setting);

        ((AppCompatActivity) SettingActivity.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "설정" + "</font>")));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userId = a_LoginMainActivity.userID;
        System.out.println(userId);

        font_two = Typeface.createFromAsset(getAssets(), "fonts/font_two.ttf"); //NHC 고도 마음체 godoM
        // 첫번째 버튼: 몸무게 설정
        loggedInWeightSetting = (Button) findViewById(R.id.loggedInWeightSetting);
        loggedInWeightSetting.setTypeface(font_two);
        loggedInWeightSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightSetting();
            }
        });

/*
        // 두번째 버튼: 전송할 전화번호 설정
        BlueTooth = (Button) findViewById(R.id.BlueTooth);
        BlueTooth.setTypeface(font_two);
        BlueTooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), bluegetheart.class);
                startActivity(intent);
            }
        });
        */

/*

        // 세번째 버튼: 심박수 블루투스 연결
        SMS_setting = (Button) findViewById(R.id.SMS_setting);
        SMS_setting.setTypeface(font_two);
        SMS_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SmsMainActivity.class);
                startActivity(intent);
            }
        });
*/


        propicsetting = (Button) findViewById(R.id.propicsetting);
        propicsetting.setTypeface(font_two);
        propicsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePicture.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(SettingActivity.this, a_LoginMainActivity.class);
            intent.putExtra("userID", a_LoginMainActivity.userID);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish(); // close this activity and return to preview activity (if there is any)
        Intent intent = new Intent(SettingActivity.this, a_LoginMainActivity.class);
        intent.putExtra("userID", a_LoginMainActivity.userID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void weightSetting() {
        final Dialog dialog = new Dialog(SettingActivity.this); //here, the name of the activity class that you're writing a code in, needs to be replaced
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
        dialog.setContentView(R.layout.dialog_weight); //setting view
        dialog.show();
        EditText weightEntered = (EditText) dialog.findViewById(R.id.weightEntered);
        Button submitButton = (Button) dialog.findViewById(R.id.submitButton);

        String savedLoggedInModeWeight = SavedSharedPreference.getLoggedInModeWeight(SettingActivity.this);
        System.out.println("saved logged in mode weight>>" + savedLoggedInModeWeight);
        if (savedLoggedInModeWeight.length() > 0) {
            weightEntered.setText(savedLoggedInModeWeight);
            if (SavedSharedPreference.getLoggedInModeWeightType(SettingActivity.this).trim().equals("lbs")) {
                RadioButton radioLbs = (RadioButton) dialog.findViewById(R.id.lbs);
                radioLbs.setChecked(true);
            } else {
                RadioButton radioKg = (RadioButton) dialog.findViewById(R.id.kg);
                radioKg.setChecked(true);
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText weightEntered = (EditText) dialog.findViewById(R.id.weightEntered);
                weightEntered = (EditText) dialog.findViewById(R.id.weightEntered);
                String weightEnteredToString = weightEntered.getText().toString();

                RadioGroup weightType = (RadioGroup) dialog.findViewById(R.id.weightType);
                int selectedId = weightType.getCheckedRadioButtonId();

                SavedSharedPreference.setLoggedInModeWeight(SettingActivity.this, weightEnteredToString);
                SavedSharedPreference.setLoggedInModeWeightType(SettingActivity.this, ((RadioButton) dialog.findViewById(selectedId)).getText().toString());
                System.out.println("saved data>>" + SavedSharedPreference.getLoggedInModeWeight(SettingActivity.this) + SavedSharedPreference.getLoggedInModeWeightType(SettingActivity.this));

                dialog.dismiss();
            }
        });
    }

}
