package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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

public class ProfilePicture extends AppCompatActivity {

    private ImageData imageData;
    private ImageView profilepic;
    private String userId;
    private Button upload;
    private Button delete;
    private String filename;
    private TextView propictitle;
    Typeface font_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity5_profile_picture);
        font_one = Typeface.createFromAsset(getAssets(), "fonts/font_one.ttf"); //TitilliumWeb-Light from Titillium Web by Accademia di Belle Arti di Urbino (1001freefonts.com)
        ((AppCompatActivity) ProfilePicture.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "프로필 사진 설정" + "</font>")));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        upload = (Button) findViewById(R.id.upload);
        delete = (Button) findViewById(R.id.delete);
        profilepic = (ImageView) findViewById(R.id.propic);
        propictitle = (TextView)findViewById(R.id.propictitle);

        userId = a_LoginMainActivity.userID.trim();
        propictitle.setText(userId+"'s profile picture");
        propictitle.setTypeface(font_one);
        System.out.println(userId);

        new BackgroundTask().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        // (로딩창 띄우기 작업 3/1) 로딩창을 띄우기 위해 선언해준다.
        ProgressDialog dialog = new ProgressDialog(ProfilePicture.this);

        String target;  //우리가 접속할 홈페이지 주소가 들어감

        @Override
        protected void onPreExecute() {
            target = "http://ggavi2000.cafe24.com/getImageFromServer.php?userId=" + userId;  //해당 웹 서버에 접속

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
                System.out.println(">>" + stringBuilder.toString().trim());
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

                int pos = result.indexOf(":");
                String test = result.substring(pos + 1);
                test = test.trim();
                if (test.charAt(0) == '[' && test.charAt(1) == ']') {
                    System.out.println("null");
                    upload.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProfilePicture.this, ProfilePictureUpload.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish();

                        }
                    });
                } else {
                    upload.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                    System.out.println("exists");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");  //아까 변수 이름
                    if (jsonArray == null) {
                        System.out.println("null");
                    } else {
                        System.out.println("exists");
                    }

                    int count = 0;
                    final String userId, image_data;

                    // 현재 배열의 원소값을 저장
                    JSONObject object;
                    object = jsonArray.getJSONObject(0);

                    // 공지사항의 Content, Name, Date에 해당하는 값을 가져와라는 뜻
                    userId = object.getString("image_tag");
                    image_data = object.getString("image_data");

                    System.out.println("check>>" + userId + "+" + image_data);
                    filename = image_data;
                    String target = "http://ggavi2000.cafe24.com/images/";
                    int index = filename.indexOf(target);
                    int subIndex = index + target.length();
                    filename = filename.substring(subIndex);

                    // 하나의 공지사항에 대한 객체를 만들어줌
                    imageData = new ImageData(userId, image_data);

                    String path = imageData.getImage_data();
                    new DownloadImageTask(profilepic).execute(path);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(ProfilePicture.this); //here, the name of the activity class that you're writing a code in, needs to be replaced
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
                            dialog.setContentView(R.layout.dialog_alert); //setting view


                            //getting textviews and buttons from dialog
                            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                            TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
                            Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
                            Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);

                            //You can change the texts on java code shown as below
                            dialogTitle.setText(" 프로필 사진 삭제 ");
                            dialogMessage.setText(" 프로필 사진을 삭제하시겠습니까? ");
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
                                                System.out.println("aaa>>"+response);
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");

                                                // 만약 삭제할 수 있다면
                                                if (success) {
                                                    new CustomConfirmDialog().showConfirmDialog(ProfilePicture.this,"삭제하였습니다.",false);
                                                    Intent intent = getIntent();
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    finish();
                                                    startActivity(intent);
                                                }

                                                // 삭제 실패
                                                else {
                                                    new CustomConfirmDialog().showConfirmDialog(ProfilePicture.this,"삭제를 실패하였습니다.",true);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    // 실질적으로 삭제할 수 있도록 생성자를 통해 객체를 만든다. (유저 ID, responseListener)
                                    // 그리고 어떤 회원이 어떤 강의를 삭제한다는 데이터는 DB에 넣어야 한다.
                                    ImageDelete imageDelete = new ImageDelete(userId, filename, responseListener);  // + ""를 붙이면 문자열 형태로 바꿈
                                    RequestQueue queue = Volley.newRequestQueue(ProfilePicture.this);
                                    queue.add(imageDelete);


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
                    });

                }

                // (로딩창 띄우기 작업 3/3)
                // 작업이 끝나면 로딩창을 종료시킨다.
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
