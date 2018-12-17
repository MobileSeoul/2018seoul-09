package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ggavi.ahnsungmin.FirstActivity;
import com.example.ggavi.ahnsungmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class a_LoginMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 이곳은 로그인을 할 때 넘어오는 액티비티다.

    // 아래 코드 3줄: 공지.java와 공지ListAdapter.java를 넣을 변수 (공지글)
    // private ListView noticeListView;
    // private 공지ListAdapter adapter;
    // private List<공지> noticeList;

    private ImageData imageData;
    public static String userID;   //모든 클래스에서 접근가능

    Typeface font_one;

    private ImageView headerUserImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_logged_in);
        ActionBar actionBar = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((AppCompatActivity) a_LoginMainActivity.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "Walk Away" + "</font>")));


        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID").toString();
        System.out.println("userID="+userID);


        font_one = Typeface.createFromAsset(getAssets(), "fonts/font_one.ttf");


        // 현재 스마트폰 화면을 세로 방향으로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //imply navigation drawer (navigation drawer적용)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set text view on navigation drawer's header (navigation drawer의 헤더 부분에 있는 text view를 셋팅)
        View headerView = navigationView.getHeaderView(0);
        TextView navUserID = (TextView) headerView.findViewById(R.id.headerUserId);
        headerUserImageView = (ImageView)headerView.findViewById(R.id.headerUserPic);

        navUserID.setText(userID);
        navUserID.setTypeface(font_one);


/*      // Floating 버튼 제거
        //set onclick listener for floating button - it will be connected to activity to walk (floating button onclick 리스너, 이걸 클릭하면 걷는 activity로 넘어가게 만들어야 함)
        FloatingActionButton fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(로그인Activity.this,"floating button onclick",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(로그인Activity.this, LoggedInWalk.class);
                intent.putExtra("userID", userID);
                finish();
                startActivity(intent);
            }
        });
*/



        // 이미지 버튼 추가
        ImageButton imageBtn = (ImageButton)findViewById(R.id.walk_imageButton);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(a_LoginMainActivity.this, LoggedInWalk.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();
            }
        });


        // 텍스트 버튼 추가
        TextView Start = (TextView) findViewById(R.id.start_text);
        Start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a_LoginMainActivity.this, LoggedInWalk.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();
            }
        });



        // 공지글 notice 부분
        // noticeListView = (ListView) findViewById(R.id.noticeListView);
        // noticeList = new ArrayList<공지>();

        /*
        DB 연결하기 전에 만들었던 예시 데이터
        noticeList.add(new 공지("공지사항 예시", "이름 예시", "날짜 생략"));
        noticeList.add(new 공지("공지사항 예시", "이름 예시", "날짜 생략"));
        */


        // adapter에 해당 List를 매칭 (각각 차례대로 매칭)
        // adapter = new 공지ListAdapter(getApplicationContext(), noticeList);
        // noticeListView.setAdapter(adapter);

        final Button runButton = (Button) findViewById(R.id.run);
        final Button courseButton = (Button) findViewById(R.id.courseButton);
        //final Button statisticsButton = (Button) findViewById(R.id.statisticsButton);
        final LinearLayout notice = (LinearLayout) findViewById(R.id.notice);  //해당 Fragment 눌렀을 때 화면의 레이아웃이 바뀌는 부분


        // 선택된 버튼만 색상을 어둡게 만들고 나머지 버튼은 밝은 색상으로 변경
        runButton.setBackground(getResources().getDrawable(R.drawable.button_back_dark_color));
        courseButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));
        //statisticsButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));




        // 1. 걷기시작 화면으로
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notice.setVisibility(View.GONE);


                // 선택된 버튼만 색상을 어둡게 만들고 나머지 버튼은 밝은 색상으로 변경
                runButton.setBackground(getResources().getDrawable(R.drawable.button_back_dark_color));
                courseButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));
                //statisticsButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // fragment 부분을, 다른 'fragment'로 대체해주는 것
                fragmentTransaction.replace(R.id.fragment, new a_LoginMainFragment());
                fragmentTransaction.commit();

                /*
                finish();
                Intent intent = new Intent(getApplicationContext(), LoggedInWalk.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                */
            }
        });


        // 2. 코스 버튼 (프래그먼트)
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 공지사항 부분이 보이지 않도록 하는 부분
                // 즉 notice 라는 LinearLayout이 사라지고 다른 Fragment가 보일 수 있도록 화면을 바꿔주는 것
                notice.setVisibility(View.GONE);


                // 선택된 버튼만 색상을 어둡게 만들고 나머지 버튼은 밝은 색상으로 변경
                runButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));
                courseButton.setBackground(getResources().getDrawable(R.drawable.button_back_dark_color));
                //statisticsButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // fragment 부분을 다른 'fragment'로 대체해주는 것
                fragmentTransaction.replace(R.id.fragment, new Fragment_loggedInRecord());
                fragmentTransaction.commit();
            }
        });

/*
        // 3. 통계 버튼
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 공지사항 부분이 보이지 않도록 하는 부분
                // 즉 notice 라는 LinearLayout이 사라지고 다른 Fragment가 보일 수 있도록 화면을 바꿔주는 것
                notice.setVisibility(View.GONE);

                // 선택된 버튼만 색상을 어둡게 만들고 나머지 버튼은 밝은 색상으로 변경
                runButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));
                courseButton.setBackground(getResources().getDrawable(R.drawable.button_back_prime_color));
                statisticsButton.setBackground(getResources().getDrawable(R.drawable.button_back_dark_color));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // fragment 부분을 new 다른 'Fragment'로 대체해주는 것
                fragmentTransaction.replace(R.id.fragment, new Fragment());
                fragmentTransaction.commit();
            }
        });
*/


        // 정상적으로 데이터베이스에 접근해서 찾아옴
        // new BackgroundTask().execute();
        new BackgroundTask2().execute();
    }


/*
    // MainFragment로 값 전달
    public Object getData() {
        return userID;
    }
*/


    class BackgroundTask2 extends AsyncTask<Void, Void, String> {
        // (로딩창 띄우기 작업 3/1) 로딩창을 띄우기 위해 선언해준다.
        ProgressDialog dialog = new ProgressDialog(a_LoginMainActivity.this);

        String target;  //우리가 접속할 홈페이지 주소가 들어감

        @Override
        protected void onPreExecute() {
            target = "http://ggavi2000.cafe24.com/getImageFromServer.php?userId="+userID.trim();  //해당 웹 서버에 접속

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
                System.out.println(">>"+stringBuilder.toString().trim());
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
                String test = result.substring(pos+1);
                test = test.trim();
                if(test.charAt(0)=='['&&test.charAt(1)==']'){
                    System.out.println("null");
                }else{
                    System.out.println("exists");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");  //아까 변수 이름
                    if(jsonArray==null){
                        System.out.println("null");
                    }else{
                        System.out.println("exists");
                    }

                    int count = 0;
                    String userId, image_data;

                    // 현재 배열의 원소값을 저장
                    JSONObject object;
                    object = jsonArray.getJSONObject(0);


                    userId = object.getString("image_tag");
                    image_data = object.getString("image_data");

                    System.out.println("check>>"+userId+"+"+image_data);

                    imageData = new ImageData(userId, image_data);

                    String path = imageData.getImage_data();
                    new DownloadImageTask(headerUserImageView).execute(path);

                }

                // (로딩창 띄우기 작업 3/3)
                // 작업이 끝나면 로딩창을 종료시킨다.
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*

    // 공지사항 데이터베이스에 접속할 수 있도록 만든 함수
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        // (로딩창 띄우기 작업 3/1) 로딩창을 띄우기 위해 선언해준다.
        ProgressDialog dialog = new ProgressDialog(로그인Activity.this);

        String target;  //우리가 접속할 홈페이지 주소가 들어감

        @Override
        protected void onPreExecute() {
            target = "http://ggavi2000.cafe24.com/NoticeList.php";  //해당 웹 서버에 접속

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

                int count = 0;
                String noticeContent, noticeName, noticeDate;

                while (count < jsonArray.length()) {
                    // 현재 배열의 원소값을 저장
                    JSONObject object = jsonArray.getJSONObject(count);

                    // 공지사항의 Content, Name, Date에 해당하는 값을 가져와라는 뜻
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");

                    // 하나의 공지사항에 대한 객체를 만들어줌
                    공지 notice = new 공지(noticeContent, noticeName, noticeDate);

                    // 리스트에 추가해줌
                    noticeList.add(notice);
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

*/

    // 두번 뒤로가기 버튼을 누르면 종료
    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        // 한번 버튼을 누른 뒤, 1.5초 이내에 또 누르면 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            return;
        }

        Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }







    //adding the menu on tool bar (액션바에서 메뉴 추가)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_after_menu, menu);
        return true;
    }

    // adding actions that will be done on clicking menu items
    // (메뉴 아이템들을 클릭할 때 발생할 이벤트 추가)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // 5번째 버튼

/*
            case R.id.course_rank:
                Intent intent5 = new Intent(getApplicationContext(), 파일이름.class);
                intent5.putExtra("userID", userID);   // 아이디 값 전달
                startActivity(intent5);
                return true;
*/

            default:
                return true;
        }
    }







    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 로그아웃 버튼
        if (id == R.id.navLogout) {
            logout();
        }

        // 나의 기록 버튼
        else if(id==R.id.navMyRecord) {
            myRecord();
        }

        // 설정 버튼
        else if(id==R.id.navSetting) {
            settingAct();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(false);
        return true;
    }

    // 로그아웃 버튼
    public void logout() {
        final Dialog dialog = new Dialog(a_LoginMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
        dialog.setContentView(R.layout.dialog_alert);

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);

        dialogTitle.setText("  로그아웃  ");
        dialogMessage.setText(" 로그아웃 하시겠습니까? ");
        dialogButton1.setText("예");
        dialogButton2.setText("아니오");

        dialog.setCanceledOnTouchOutside(false); //to prevent dialog getting dismissed on outside touch
        dialog.setCancelable(false); //to prevent dialog getting dismissed on back button
        dialog.show();

        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SavedSharedPreference.clearId(a_LoginMainActivity.this);
                Intent intent = new Intent(a_LoginMainActivity.this,
                        FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);

            }
        });

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // 나의 기록 버튼
    public void myRecord() {
        Intent intent = new Intent(a_LoginMainActivity.this, LoggedInRecord.class);
        intent.putExtra("userID",userID);
        finish();
        startActivity(intent);
    }

    // 설정 버튼
    public void settingAct() {
        finish();
        Intent intent = new Intent(a_LoginMainActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}