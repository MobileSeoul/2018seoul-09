package com.example.ggavi.ahnsungmin.nomal_mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ggavi.ahnsungmin.FirstActivity;
import com.example.ggavi.ahnsungmin.R;

import org.json.JSONObject;

public class a_LoginActivity extends AppCompatActivity {

    private boolean saveLoginData;
    private String id;
    private String pwd;
    private CheckBox checkBox; //for saving login information (로그인 정보 저장하기)
    private SharedPreferences appData;

    private EditText idText;
    private EditText passwordText;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity1_login);
        ((AppCompatActivity) a_LoginActivity.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "로그인" + "</font>")));

        //getting the set value (설정 값 불러오기)
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();


        // (2)로그인 창에서 회원가입 버튼을 눌렀을 때 화면이 넘어가는 부분
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(a_LoginActivity.this, a_RegisterActivity.class);
                a_LoginActivity.this.startActivity(registerIntent);
            }
        });


        // (5)아이디 텍스트를 매칭시켜준다 (아이디를 입력받는 부분)
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        if (saveLoginData) {
            idText.setText(id);
            passwordText.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }


/*

        // EditText 엔터키 막기
        idText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == event.KEYCODE_ENTER)
                {
                    return true;
                }

                return false;
            }
        });

*/



        // (5)로그인 버튼을 눌렀을 때 발생하는 이벤트 처리
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // jsonResponse 이놈은 해당 결과는 받아온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            // success 이놈이 나올 경우 (로그인에 성공한 경우)
                            if (success) {
                                save(); //if logged in successfully, save the data (로그인 성공하면 데이터를 저장)
                                SavedSharedPreference.setId(a_LoginActivity.this,userID);

                               new CustomConfirmDialog().showConfirmDialog(a_LoginActivity.this,"로그인에 성공하였습니다.",false);

                                // 화면 전환 (로그인창 -> 메인창)
                                Intent intent = new Intent(a_LoginActivity.this, a_LoginMainActivity.class);

                                // (12)userID에 대한 정보를 보내줌
                                intent.putExtra("userID", userID);

                                a_LoginActivity.this.startActivity(intent);
                                finish(); //현재 액티비티 닫기
                            }


                            // 로그인에 실패한 경우
                            // 로그인에 실패한 경우
                            else {
                                new CustomConfirmDialog().showConfirmDialog(a_LoginActivity.this,"계정을 다시 확인하세요.",true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                // 실제로 로그인을 보낼 수 있는 로그인 리퀘스트
                a_LoginRequest loginRequest = new a_LoginRequest(userID, userPassword, responseLister);
                RequestQueue queue = Volley.newRequestQueue(a_LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }


    // a function that saves the set data (설정값을 저장하는 함수)
    private void save() {

        // only SharedPreferences is not enough, use Editor (SharedPreferences 객체만으론 저장 불가능 Editor 사용)
        SharedPreferences.Editor editor = appData.edit();

        //set the data- if saved data exists, overwrite (데이터 저장, 이미 저장되어 있으면 덮어씌움)
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        editor.putString("PWD", passwordText.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // a function that loads the set data (설정값을 불러오는 함수)
    private void load() {
        //get the data - if no data, it will set as default value (데이터를 받아옴. 없다면 기본값)
        //
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        pwd = appData.getString("PWD", "");
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(a_LoginActivity.this,FirstActivity.class);
        startActivity(intent);
        return;

    }


}