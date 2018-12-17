package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ggavi.ahnsungmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoggedInRecord extends AppCompatActivity {

    private static String userID = "";

    private ListView recordListView;
    private RecordListAdapter adapter;
    private List<Record> recordList;

    private TextView noRecAlert;

    private Button deleteAllRec;
    private TextView titleForRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_logged_in_record);
        recordListView = (ListView) findViewById(R.id.recordListView);
        ((AppCompatActivity) LoggedInRecord.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "나의 기록 관리" + "</font>")));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recordList = new ArrayList<Record>();

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID").toString();

        noRecAlert = (TextView)findViewById(R.id.noRecAlert);

        deleteAllRec = (Button)findViewById(R.id.deleteAllRec);

        titleForRecord = (TextView)findViewById(R.id.titleForRecord);

        titleForRecord.setText("사용자 "+userID+"님의 상세 기록입니다.");

        deleteAllRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllRec();
            }
        });
        // adapter에 해당 List를 매칭 (각각 차례대로 매칭)
        adapter = new RecordListAdapter(getApplicationContext(), recordList);
        recordListView.setAdapter(adapter);

        new BackgroundTask().execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        // (로딩창 띄우기 작업 3/1) 로딩창을 띄우기 위해 선언해준다.
        ProgressDialog dialog = new ProgressDialog(LoggedInRecord.this);

        String target;  //우리가 접속할 홈페이지 주소가 들어감

        @Override
        protected void onPreExecute() {
            target = "http://ggavi2000.cafe24.com/RecordList.php?userId="+ userID;  //해당 웹 서버에 접속

            // (로딩창 띄우기 작업 3/2)
            dialog.setMessage("로딩중");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // 해당 서버에 접속할 수 있도록 URL을 커넥팅 한다.
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                // 넘어오는 결과값을 그대로 저장
                InputStream inputStream = httpURLConnection.getInputStream();

                // 해당 inputStream에 있던 내용들을 버퍼에 담아서 읽을 수 있도록 해줌
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // 이제 temp에 하나씩 읽어와서 그것을 문자열 형태로 저장
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                // null 값이 아닐 때까지 계속 반복해서 읽어온다.
                while ((temp = bufferedReader.readLine()) != null) {
                    // temp에 한줄씩 추가하면서 넣어줌
                    stringBuilder.append(temp + "\n");
                }

                // 끝난 뒤 닫기
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();  //인터넷도 끊어줌
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override  //해당 결과를 처리할 수 있는 onPostExecute()
        public void onPostExecute(String result) {
            try {
                // 해당 결과(result) 응답 부분을 처리
                JSONObject jsonObject = new JSONObject(result);

                // response에 각각의 공지사항 리스트가 담기게 됨
                JSONArray jsonArray = jsonObject.getJSONArray("response");  //아까 변수 이름

                System.out.println("length of jsonArray>>"+jsonArray.length());
                if(jsonArray.length()==0){
                    noRecAlert.setVisibility(View.VISIBLE);
                    deleteAllRec.setVisibility(View.GONE);
                }else{
                    noRecAlert.setVisibility(View.GONE);
                    deleteAllRec.setVisibility(View.VISIBLE);
                }
                int count = 0;
                String userId, pedometer, distance, calorie, time, speed, date, progress,datetime;

                while (count < jsonArray.length()) {
                    // 현재 배열의 원소값을 저장
                    JSONObject object = jsonArray.getJSONObject(count);

                    // 공지사항의 Content, Name, Date에 해당하는 값을 가져와라는 뜻

                    userId = object.getString("userId");
                    pedometer= object.getString("pedometer");
                    distance = object.getString("distance");
                    calorie = object.getString("calorie");
                    time = object.getString("time");
                    speed = object.getString("speed");
                    date = object.getString("date");
                    int pos = date.indexOf(" ");
                    date = date.substring(0,pos);
                    datetime= object.getString("date");
                    progress = object.getString("progress");

                    // 하나의 공지사항에 대한 객체를 만들어줌
                    Record record = new Record(userId, pedometer, distance, calorie, time, speed, date, progress,datetime);

                    // 리스트에 추가해줌
                    recordList.add(record);
                    adapter.notifyDataSetChanged();
                    count++;
                }

                // (로딩창 띄우기 작업 3/3)
                // 작업이 끝나면 로딩창을 종료시킨다.
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //adding the menu on tool bar (액션바에서 메뉴 추가)

    // adding actions that will be done on clicking menu items
    // (메뉴 아이템들을 클릭할 때 발생할 이벤트 추가)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                Intent intent = new Intent(LoggedInRecord.this, a_LoginMainActivity.class);
                intent.putExtra("userID", a_LoginMainActivity.userID);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;

            default:
                return true;
        }

    }
    @Override
    public void onBackPressed() {
        finish(); // close this activity and return to preview activity (if there is any)
        Intent intent = new Intent(LoggedInRecord.this, a_LoginMainActivity.class);
        intent.putExtra("userID", a_LoginMainActivity.userID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void deleteAllRec(){
        final Dialog dialog = new Dialog(LoggedInRecord.this); //here, the name of the activity class that you're writing a code in, needs to be replaced
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
        dialog.setContentView(R.layout.dialog_alert); //setting view


        //getting textviews and buttons from dialog
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);

        //You can change the texts on java code shown as below
        dialogTitle.setText(" 모든 기록 삭제 ");
        dialogMessage.setText("사용자 "+userID+"님의 모든 기록을 삭제하시겠습니까?");
        dialogButton1.setText("삭제");
        dialogButton2.setText("취소");

        dialog.setCanceledOnTouchOutside(false); //dialog won't be dismissed on outside touch
        dialog.setCancelable(false); //dialog won't be dismissed on pressed back
        dialog.show(); //show the dialog

        //here, I will only dismiss the dialog on clicking on buttons. You can change it to your code.
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //your code here
                dialog.dismiss(); //to dismiss the dialog
                // 정상적으로 ID 값을 입력했을 경우 중복체크 시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // 해당 웹사이트에 접속한 뒤 특정한 response(응답)을 다시 받을 수 있도록 한다
                        try {
                            System.out.println("response>>"+response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            // 만약 삭제할 수 있다면
                            if (success) {
                                new CustomConfirmDialog().showConfirmDialog(LoggedInRecord.this,"삭제하였습니다.",false);
                                Intent intent = getIntent();
                                intent.putExtra("userID",userID);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                startActivity(intent);
                            }

                            // 삭제 실패
                            else {
                                new CustomConfirmDialog().showConfirmDialog(LoggedInRecord.this,"삭제를 실패하였습니다.",true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                RecordDelete recordDelete = new RecordDelete(userID, responseListener);  // + ""를 붙이면 문자열 형태로 바꿈
                RequestQueue queue = Volley.newRequestQueue(LoggedInRecord.this);
                queue.add(recordDelete);


            }
        });

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //your code here
                dialog.dismiss(); //to dismiss the dialog

            }
        });
    }


}
