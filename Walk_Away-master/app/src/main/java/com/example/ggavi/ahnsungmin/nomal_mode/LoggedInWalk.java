package com.example.ggavi.ahnsungmin.nomal_mode;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ggavi.ahnsungmin.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LoggedInWalk extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, SensorEventListener, StepListener {

    private String userID;
    // private BroadcastReceiver broadcastReceiver; 아두이노 부품 연동시

    private GoogleMap mMap; //map (맵)
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker; //marker (마커)
    private TextView distanceTv;    //distance text view (거리 텍스트 뷰)
    private TextView calTv;         //calorie text view (칼로리 텍스트 뷰)
    private TextView stepsTv;       //step text view (걸음수 - 만보기 텍스트 뷰)
    private TextView timeTv;        //time text view (시간 텍스트 뷰)
    private LinearLayout achievementLayout;
    private AlertDialog.Builder choosedistanse;  //목표설정에 공백 넣으면 띄울 Alert 창을 위해 선언

    private DecimalFormat kcalFormat = new DecimalFormat("#0.00"); //calorie -> 0.00 format (칼로리 -> 0.00 형식)
    private DecimalFormat kmFormat = new DecimalFormat("#0.000");//km-distance -> 0.000 format (km-거리 -> 0.000 형식)


    private PolylineOptions polylineOptions; //poly line option
    private StepDetector simpleStepDetector; //step detector (만보기)
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps; //step numbers (걸음 수)


    private static GPSListener gpsListener = null; //object for tracking a route (루트를 트래킹 할 객체)

    List<LatLng> pointsOnRoute = new ArrayList<>(); //points passed on walking the route (걸을 때 지나간 포인트들)

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //boolean to decide whether tracking or not (트래킹하고 있는지 아닌지 결정하는 boolean)
    private Button trackingButton;
    private boolean isTracking = false;

    //variables for counting time (시간 카운트를 위한 변수들)
    long millisecondTime, startTime, timeBuff, updateTime = 0L;
    int seconds, minutes, milliSeconds;
    Handler handler;

    private boolean isGoalEntered = false;
    private double goal = 0;
    private TextView achievementPercentage;
    private ProgressBar progressBar;


/*
    // BTHeartRate 아두이노 심박수 체크 추가
    // 클릭시에 다이얼로그로 블루투스 실행 여부 묻는 다이얼로그
    private Dialog startBT_Dialog;
    private Button dialog_reg;
    private Button dialog_cancel;

    // 블루투스 관련 된 것들
    private List<BTSmsNumber> smsNumberList;

    BTSmsNumber smsNumber;
    private int alertcount=0;
    static final int REQUEST_ENABLE_BT = 10;
    BluetoothAdapter mBluetoothAdapter;
    int mPairedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    BluetoothDevice mRemoteDevice;
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;

    MediaPlayer mp;

    Thread mWorkerThread = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter = '\n';
    byte[] readBuffer;
    int readBufferPosition;

    private String PHnum;//폰번호를 받아오는 사이트
    private String PHtext;
    TextView mEditReceive, mEditSend;

    // 메세지에 위치 정보 전송하기 위한 위도 경도 저장
    double lastLocationLat;
    double lastLocationLng;
    LocationManager locationManager;
    List<Address> list;
    Geocoder geocoder;

    //------------------------------------------
    bt b;
*/



    // 심박수 버튼 추가
    // private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


        // 상단 액션바(타이틀 바) 없애려고 넣음
        getSupportActionBar().hide();

        setContentView(R.layout.login_activity3_walk);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");


        ((AppCompatActivity) LoggedInWalk.this).getSupportActionBar().setTitle((Html.fromHtml("<font color='#ffffff'>" + "걷기모드" + "</font>")));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        buildGoogleApiClient(); //GoogleApiClient를 빌드

        distanceTv = (TextView) findViewById(R.id.distanceL);
        calTv = (TextView) findViewById(R.id.calL);
        stepsTv = (TextView) findViewById(R.id.stepsL);
        timeTv = (TextView) findViewById(R.id.timeL);

        trackingButton = (Button) findViewById(R.id.trackL);

        achievementLayout = (LinearLayout) findViewById(R.id.achievementLayout);
        achievementPercentage = (TextView) findViewById(R.id.achievementPercentage);
        progressBar = (ProgressBar) findViewById(R.id.achievementProgressBar);

        progressBar.setMax(100);

        //registering step counting sensor (만보기 센서하게 레지스터)
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        handler = new Handler();

        //confirming if GPS is connected (GPS연결 여부 확인하기)
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //getting whether gps is connected or not (연결 여부 가져오기)
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {//if not connected (연결 안된 경우)

            //shows a dialog to connect gps (gps연결 하기 위해 dialog 보여줌)

            final Dialog dialog = new Dialog(LoggedInWalk.this); //here, the name of the activity class that you're writing a code in, needs to be replaced
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
            dialog.setContentView(R.layout.dialog_alert); //setting view


            //getting textviews and buttons from dialog
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
            TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
            Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);

            //You can change the texts on java code shown as below
            dialogTitle.setText(" GPS연결 ");
            dialogMessage.setText(" 이 기능은 GPS 연결이 필요합니다. ");
            dialogButton1.setText("연결");
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
                    //get gps
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.mapL);
                    mapFragment.getMapAsync(LoggedInWalk.this);
                }
            });

            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //your code here
                    dialog.dismiss(); //to dismiss the dialog
                    finish();
                    Intent intent = new Intent(LoggedInWalk.this, a_LoginMainActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent); //force back to first activity
                }
            });

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapL);
            mapFragment.getMapAsync(LoggedInWalk.this);
        }


/*
        // 심박수 측정 추가 BTHeartRate
        // 블루투스 음악 관련 것들
        mEditReceive = (TextView)findViewById(R.id.heartrateL);
        // mEditSend = (EditText)findViewById(R.id.sendText);

        mp=MediaPlayer.create(this,R.raw.a);
        ImageButton heartPicL = (ImageButton)findViewById(R.id.heartPicL);
        heartPicL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 하트 버튼을 눌렀을 때 다이얼 로그 창이 나옴.
                startBT_Dialog= new Dialog(LoggedInWalk.this);
                startBT_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                // 다이얼 화면 구성하기
                startBT_Dialog.setContentView(R.layout.start_bt_dialog);
                dialog_reg=(Button)startBT_Dialog.findViewById(R.id.dialog_reg);
                dialog_cancel=(Button)startBT_Dialog.findViewById(R.id.dialog_cancel);

                dialog_reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"실행되었습니다.",Toast.LENGTH_SHORT).show();
                        b = new bt();
                        b.checkBluetooth();
                        startBT_Dialog.dismiss();
                    }
                });

                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //취소시에 종료 토스트 출력하기.
                        Toast.makeText(getApplicationContext(),"취소되었습니다.",Toast.LENGTH_SHORT).show();

                        //다이얼로그 종료
                        startBT_Dialog.dismiss();
                    }
                });

                startBT_Dialog.show();
            }
        });
*/





/*
        //-------------메세지에 위치정보 더하기
        geocoder = new Geocoder(this);//위도 경도를 입력해서 주소 받기 위한것.
        list = null;
        locationManager = (LocationManager) LoggedInWalk.this.getSystemService(Context.LOCATION_SERVICE);//경도 위도 구하기 위해서
*/


        /*
        // (심박수 버튼 추가) Floating Action Button 적용
        fab = (FloatingActionButton) findViewById(R.id.fab_login_walk);

        // 이벤트 적용
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bluegetheart.class);
                startActivity(intent);
            }
        });
        */

    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //when Map is ready(Map이 ready되어 있는 상태)
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        trackingButton.setOnClickListener(new View.OnClickListener() {//start tracking (트래킹 시작)
            @Override
            public void onClick(View view) {

                // 목표설정에 공백 넣으면 띄울 Alert 창을 위해 선언
                choosedistanse = new AlertDialog.Builder(LoggedInWalk.this);

                if (!isTracking) {
                    final Dialog dialog = new Dialog(LoggedInWalk.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_goal);

                    dialog.setCanceledOnTouchOutside(false); //to prevent dialog getting dismissed on outside touch
                    dialog.setCancelable(false); //to prevent dialog getting dismissed on back button
                    dialog.show();

                    final EditText goalEntered = (EditText) dialog.findViewById(R.id.goalEntered);
                    Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                    Button skipButton = (Button) dialog.findViewById(R.id.skipButton);

                    skipButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isGoalEntered = false;
                            dialog.dismiss();
                        }
                    });

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // 목표설정에서 공백 입력시 뜨는 Alert 창
                            isGoalEntered = true;
                            System.out.println("푸푸씨이이1: " + goalEntered.getText().toString());


                            // 사용자가 공백을 넣은 경우
                            if(goalEntered.getText().toString().equals(""))
                            {
                                choosedistanse.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                choosedistanse.setMessage("값을 입력해주세요.");
                                choosedistanse.show();
                            }


                            // 사용자가 0을 넣은 경우
                            else if(goalEntered.getText().toString().equals("0"))
                            {
                                choosedistanse.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                choosedistanse.setMessage("값을 입력해주세요.");
                                choosedistanse.show();
                            }


                            else  // 정상적으로 입력한 경우
                            {
                                System.out.println("푸푸씨이이2: " + goalEntered.getText().toString());
                                goal = Double.parseDouble(goalEntered.getText().toString());

                                dialog.dismiss();
                            }
                        }
                    });


                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface arg0) {
                            // do something
                            trackingButton.setText(" STOP ");
                            trackingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stop2, 0, 0, 0);
                            trackingButton.setBackgroundResource(R.drawable.button_background);
                            isTracking = true; //tracking started (트래킹시작)

                            if (isGoalEntered) {
                                achievementLayout.setVisibility(View.VISIBLE);
                                System.out.println("goal set>>" + goal);
                            } else {
                                achievementLayout.setVisibility(View.GONE);
                                System.out.println("goal is not entered");
                            }

                            //setting system time as start time (시스템의 시간을 가져와 startTime로 설정)
                            startTime = SystemClock.uptimeMillis();
                            //handler starts (핸들러 시작)
                            handler.postDelayed(runnable, 0);


                            //step counter registered (걸음 수 측정 레지스터)
                            numSteps = 0;
                            sensorManager.registerListener(LoggedInWalk.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                            //requesting location updates according to sdk version & whether permission is granted or not
                            //(sdk버전 & permission 여부에 따라 위치 업데이트를 요청)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(LoggedInWalk.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                        ContextCompat.checkSelfPermission(LoggedInWalk.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                                PackageManager.PERMISSION_GRANTED) {
                                    mMap.setMyLocationEnabled(true);
                                    mLocationRequest = new LocationRequest();

                                    mLocationRequest.setInterval(10000).setFastestInterval(10000 / 2).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    //create object for tracking (트래킹을 위한 객체 생성)
                                    gpsListener = new GPSListener();
                                    if (ContextCompat.checkSelfPermission(LoggedInWalk.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsListener);
                                    }


                                } else {
                                    checkLocationPermission();
                                    mLocationRequest = new LocationRequest();

                                    mLocationRequest.setInterval(10000).setFastestInterval(10000 / 2).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    //create object for tracking (트래킹을 위한 객체 생성)
                                    gpsListener = new GPSListener();
                                    if (ContextCompat.checkSelfPermission(LoggedInWalk.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsListener);
                                    }
                                    //setting system time as start time (시스템의 시간을 가져와 startTime로 설정)
                                    startTime = SystemClock.uptimeMillis();
                                    //handler starts (핸들러 시작)
                                    handler.postDelayed(runnable, 0);

                                    //step counter registered (걸음 수 측정 레지스터)
                                    numSteps = 0;
                                    sensorManager.registerListener(LoggedInWalk.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                                    // mMap.setMyLocationEnabled(true);
                                }
/*
if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
    googleMap.setMyLocationEnabled(true);
    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
} else {
    Toast.makeText(this, R.string.error_permission_map, Toast.LENGTH_LONG).show();
}
 */


                            } else {
                                //    buildGoogleApiClient();
                                mMap.setMyLocationEnabled(true);
                                mLocationRequest = new LocationRequest();
                                mLocationRequest.setInterval(10000).setFastestInterval(10000 / 2).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                gpsListener = new GPSListener();
                                if (ContextCompat.checkSelfPermission(LoggedInWalk.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsListener);
                                }
                            }

                        }
                    });
                } else {
                    trackingButton.setText("START");
                    trackingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_icon, 0, 0, 0);
                    trackingButton.setBackgroundResource(R.drawable.button_background);
                    isTracking = false; //tracking stopped (트래킹 멈춤)
                    achievementLayout.setVisibility(View.GONE);
                    if (mGoogleApiClient != null) {

                        final String stepsRecord = stepsTv.getText().toString();
                        final String distanceRecord = distanceTv.getText().toString();

                        final String calRecord = calTv.getText().toString();
                        final String timeRecord = timeTv.getText().toString();
                        final String progressRecord;
                        if(isGoalEntered) {
                            progressRecord = achievementPercentage.getText().toString();
                        }else{
                            progressRecord = "skipped";
                        }

                        //getting minutes, seconds and milliseconds from time record (시간 기록에서 minutes, seconds, milliseconds로 다시 나눔)
                        String time = timeRecord;
                        int pos_1 = time.indexOf(":");
                        String minute = time.substring(0, pos_1);
                        System.out.println(minute);
                        String aa = time.substring(pos_1);
                        String aa2 = aa.substring(1);
                        int pos_2 = aa2.indexOf(":");
                        String second = aa2.substring(0, pos_2);
                        System.out.println(second);
                        String aa3 = aa2.substring(pos_2);
                        String millisecond = aa3.substring(1);
                        System.out.println(millisecond);

                        //minute + second/60 + (millisecond/60)/1000 = total time
                        double totalTime = Double.parseDouble(minute) + (Double.parseDouble(second) / 60) + ((Double.parseDouble(millisecond) / 60) / 1000);
                        System.out.println("total time>>" + totalTime);
                        System.out.println("distance>>" + distanceRecord);
                        double speed = (Double.parseDouble(distanceRecord) / totalTime) * 60;


                        System.out.println("speed>>" + speed);
                        //speed = kmFormat.format(speed);
                        final String speedRecord = kmFormat.format(speed);
                        //  speedRecord = Double.toString((Double.parseDouble(distanceRecord) / Double.parseDouble(timeMinute)) * 60);

                        System.out.println("formatted>>" + speedRecord);
                        //System.out.println("speed>>"+speedRecord);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                // 해당 웹사이트에 접속한 뒤 특정한 response(응답)을 다시 받을 수 있도록 한다
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
/*
                                    // 푸푸씨가 보기 싫다고 지우라고 해서 지움
                                    if (success) {
                                         Toast.makeText(LoggedInWalk.this, "success", Toast.LENGTH_LONG).show();
                                    }

                                    else {
                                        Toast.makeText(LoggedInWalk.this, "failed", Toast.LENGTH_LONG).show();
                                    }
*/

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        // 실질적으로 접속할 수 있도록 생성자를 통해 객체를 만든다.
                        AddLoggedInRecordRequest request = new AddLoggedInRecordRequest(userID, stepsRecord, distanceRecord, calRecord, timeRecord, speedRecord, progressRecord,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(LoggedInWalk.this);
                        queue.add(request);

                        //remove location updates (위치 업데이트 지우기)
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsListener);

                        //unregistering step counter (걸음 수 측정 기능 없애기)
                        sensorManager.unregisterListener(LoggedInWalk.this);

                        //location disable
                        checkLocationPermission();
                        mMap.setMyLocationEnabled(false);

                        //displaying distance, kcal, steps etc. (걸어온 거리, 칼로리, 걸음 수 보여줌)
                        final Dialog dialog = new Dialog(LoggedInWalk.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.login_record_dialog);

                        TextView dialogSteps = (TextView) dialog.findViewById(R.id.dialogSteps);
                        TextView dialogDistance = (TextView) dialog.findViewById(R.id.dialogDistance);
                        TextView dialogCal = (TextView) dialog.findViewById(R.id.dialogCal);
                        TextView dialogTime = (TextView) dialog.findViewById(R.id.dialogTime);

                        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                        dialogTitle.setText(userID + "님의 Walk Away 기록");


                        dialogSteps.setText("만보기 기록: " + stepsRecord + " 보");
                        dialogDistance.setText("거리 기록: " + distanceRecord + " km");
                        dialogCal.setText("칼로리 소모량 기록: " + calRecord + " 칼로리");
                        dialogTime.setText("시간 기록: " + timeRecord);

                        //resetting all values (모두 다시 리셋팅)
                        stepsTv.setText("0");
                        distanceTv.setText("0.000");
                        calTv.setText("0.00");
                        timeTv.setText("00:00:000");
                        pointsOnRoute.clear();
                        handler.removeCallbacks(runnable);
                        gpsListener.setPreviousLocation(null);
                        mMap.clear();
                        Button dialogHome = (Button) dialog.findViewById(R.id.dialogHome);
                        dialogHome.setText("메인화면");

                        dialog.setCanceledOnTouchOutside(false); //to prevent dialog getting dismissed on outside touch
                        dialog.setCancelable(false); //to prevent dialog getting dismissed on back button
                        dialog.show();
                        Button dialogButton = (Button) dialog.findViewById(R.id.dialogBack);

                        Button dialogShare = (Button) dialog.findViewById(R.id.dialogShare);
                        // if button is clicked, close dialog (클릭 시 dialog 끔)
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        // if button is clicked, intent to HOME (클릭 시 홈으로 이동)
                        dialogHome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                finish();
                                Intent intent = new Intent(view.getContext(), a_LoginMainActivity.class);
                                intent.putExtra("userID", userID);
                                startActivityForResult(intent, 0);
                            }
                        });

                        //if button is clicked share functions will be popped up (클릭 시 공유 기능 나옴)
                        dialogShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String textBody = "유저 아이디 " + userID + "님의 Walk Away 기록\n\n만보기 기록: " + stepsRecord + " 보\n거리 기록: " + distanceRecord + " km\n칼로리 소모량 기록: " + calRecord + " 칼로리\n시간 기록: " + timeRecord;
                                //link can be added later
                                String textSubject = "[Walk Away] 나의 기록";
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, textSubject);
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, textBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                            }
                        });


                    }
                }

            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkLocationPermission() { //for checking permission (permission 체크 용)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoggedInWalk.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    protected void buildGoogleApiClient() { //for building google api client (google api client빌드)
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) { //when step counts increased (걸음 수 증가 시)
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel( //update (업데이트)
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        //increasing steps (걸음 수 증가 시키기)
        numSteps++;
        //setting text for steps (걸음 수 보여주기
        stepsTv.setText(" " + numSteps);

    }

    //Runnable for counting time (시간을 카운트하는 Runnable)
    public Runnable runnable = new Runnable() {

        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);
            timeTv.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));
            handler.postDelayed(this, 0);
        }

    };


    private class GPSListener implements LocationListener { //class for tracking (트래킹 하는 클래스)
        public void setPreviousLocation(Location previousLocation) {
            this.previousLocation = previousLocation;
        }

        private Location previousLocation = null; //previous location (이전 위치)
        private double totalDistance = 0D; //total distance (총 거리)
        private double totalDistance_hidden = 0D; //hidden distance to show counting (카운팅을 보여 줄 hidden distance)
        private double dis = 0D; //distance (거리)
        private double cal = 0.0; //calorie (칼로리)


        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                if ((location.getAccuracy() > 15 && this != null)) { //if gps connection is week (gps수신이 약한 경우)
                    //Toast.makeText(LoggedInWalk.this, "GPS 수신이 약하기 때문에 거리 측정이 어렵습니다.", Toast.LENGTH_SHORT).show(); //
                    //mentioning "GPS connection is week. It is difficult to predict the distance.
                }

                if (previousLocation != null/* && location.getAccuracy() < 15*/) { //previous location is not null (이전 위치가 null이 아님)

                    //calculate the values and show them on UI (값 계산하고 UI에 보여주기)
                    cal = totalDistance_hidden * (getCalorieRate() * 0.001);
                    // Toast.makeText(LoggedInWalk.this,"calorie rate:"+getCalorieRate(),Toast.LENGTH_LONG).show();
                    calTv.setText("" + kcalFormat.format(cal));


                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                        mCurrLocationMarker = null;
                    }
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    dis = location.distanceTo(previousLocation);
                    if (dis < 1.0) {
                        dis = 0D;
                    }
                    totalDistance += dis / 1000; //showing distance in km (km단위로 보여주기)
                    totalDistance_hidden += dis;
                    distanceTv.setText(" " + kmFormat.format(totalDistance));

                    if (isGoalEntered) {
                        Double percentage = (100 * totalDistance) / goal;
                        if(percentage<100){
                            achievementPercentage.setText(String.valueOf(percentage.intValue()));
                            progressBar.setProgress(percentage.intValue());
                        }
                    }
                    pointsOnRoute.add(latLng);
                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.MAGENTA);
                    polylineOptions.width(7);
                    Polyline route = mMap.addPolyline(polylineOptions);
                    route.setPoints(pointsOnRoute);

                    MarkerOptions op = new MarkerOptions();
                    op.title("현재 위치입니다.");
                    op.position(latLng);
                    op.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    mCurrLocationMarker = mMap.addMarker(op);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                //setting current location as previous location for next location update (다음 위치 업데이트를 위해 현재 위치를 이전 위치로 세팅)
                this.previousLocation = location;

            }
        }
    }

    public double getCalorieRate() {

        double calorieRate = 0.00;
        String savedLoggedInWeight = SavedSharedPreference.getLoggedInModeWeight(LoggedInWalk.this);
        double weightInKm = 0.0;
        if (savedLoggedInWeight.length() == 0) {
            calorieRate = 55.511;
            return calorieRate;
        } else {
            weightInKm = Double.parseDouble(savedLoggedInWeight);
            if (SavedSharedPreference.getLoggedInModeWeightType(LoggedInWalk.this).trim().equals("lbs")) {
                weightInKm = weightInKm * 0.453592;
            }
            if (weightInKm <= 52) {
                calorieRate = 31.25;
            } else if (weightInKm >= 53 && weightInKm <= 59) {
                calorieRate = 36.25;
            } else if (weightInKm >= 60 && weightInKm <= 66) {
                calorieRate = 40.625;
            } else if (weightInKm >= 67 && weightInKm <= 73) {
                calorieRate = 45;
            } else if (weightInKm >= 74 && weightInKm <= 80) {
                calorieRate = 49.375;
            } else if (weightInKm >= 81 && weightInKm <= 86) {
                calorieRate = 53.75;
            } else if (weightInKm >= 87 && weightInKm <= 93) {
                calorieRate = 58.125;
            } else if (weightInKm >= 94 && weightInKm <= 100) {
                calorieRate = 62.5;
            } else if (weightInKm >= 101 && weightInKm <= 107) {
                calorieRate = 66.875;
            } else if (weightInKm >= 108 && weightInKm <= 130) {
                calorieRate = 76.25;
            } else if (weightInKm >= 131) {
                calorieRate = 90.625;
            }
        }
        return calorieRate;
    }


    // 두번 뒤로가기 버튼을 누르면 종료
    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        // 한번 버튼을 누른 뒤, 1.5초 이내에 또 누르면 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            Intent intent = new Intent(LoggedInWalk.this, a_LoginMainActivity.class);
            intent.putExtra("userID", userID);
            startActivityForResult(intent, 0);
            return;
        }

        Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 메인 화면으로 넘어갑니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    public double getTotalTime(String timeRecord) {
        String time = timeRecord;
        int pos_1 = time.indexOf(":");
        String minute = time.substring(0, pos_1);
        System.out.println(minute);
        String aa = time.substring(pos_1);
        String aa2 = aa.substring(1);
        int pos_2 = aa2.indexOf(":");
        String second = aa2.substring(0, pos_2);
        System.out.println(second);
        String aa3 = aa2.substring(pos_2);
        String millisecond = aa3.substring(1);
        System.out.println(millisecond);

        //minute + second/60 + (millisecond/60)/1000 = total time
        double totalTime = Double.parseDouble(minute) + (Double.parseDouble(second) / 60) + ((Double.parseDouble(millisecond) / 60) / 1000);
        return totalTime;
    }






    /*

    // 심박수 측정 추가
    // 블루투스,메세지,위치정보를 메세지에 전송 기능
    class bt{
        public bt(){
            new BT_BackgroundTask().execute();
        }
        //블루투스가 켜져있는지 지원하는지 확인 활성화 상태로 바꾸기 위한 부분
        void checkBluetooth(){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){
                // 장치가 블루투스를 지원하지 않는 경우
                Toast.makeText(getBaseContext(), "장치가 블루투스를 지원하지 않습니다", Toast.LENGTH_SHORT).show();
                return;      // 종료
            }
            else {
                // 장치가 블루투스를 지원하는 경우
                if (!mBluetoothAdapter.isEnabled()) {
                    // 블루투스를 지원하지만 비활성 상태인 경우
                    // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                    Intent enableBtIntent =
                            new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else {
                    // 블루투스를 지원하며 활성 상태인 경우
                    // 페어링 된 기기 목록을 보여주고 연결할 장치를 선택
                    selectDevice();
                }
            }
        }


        //페어링된 디바이스 목록
        void selectDevice(){
            mDevices = mBluetoothAdapter.getBondedDevices();
            mPairedDeviceCount = mDevices.size();

            if(mPairedDeviceCount == 0){
                // 페어링 된 장치가 없는 경우
                Toast.makeText(getBaseContext(), "페어링된 장치가 없습니다", Toast.LENGTH_SHORT).show();
                return;      // 종료
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(LoggedInWalk.this);
            builder.setTitle("블루투스 장치 선택");

            // 페어링 된 블루투스 장치의 이름 목록 작성
            List<String> listItems = new ArrayList<String>();
            for (BluetoothDevice device : mDevices) {
                listItems.add(device.getName());
            }
            listItems.add("취소");		// 취소 항목 추가

            final CharSequence[] items =
                    listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int item){
                    if(item == mPairedDeviceCount){
                        // 연결할 장치를 선택하지 않고 ‘취소’를 누른 경우
                        Toast.makeText(getBaseContext(), "취소를 누르셨습니다", Toast.LENGTH_SHORT).show();
                        return;      // 종료
                    }
                    else{
                        // 연결할 장치를 선택한 경우
                        // 선택한 장치와 연결을 시도함
                        connectToSelectedDevice(items[item].toString());
                    }
                }
            });

            builder.setCancelable(false);	// 뒤로 가기 버튼 사용 금지
            AlertDialog alert = builder.create();
            alert.show();
        }

        //디바이스를 사용하기 위한 부분
        void connectToSelectedDevice(String selectedDeviceName){
            mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try{
                // 소켓 생성
                mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
                // RFCOMM 채널을 통한 연결

                mSocket.connect();


                // 데이터 송수신을 위한 스트림 얻기
                mOutputStream = mSocket.getOutputStream();
                mInputStream = mSocket.getInputStream();

                // 데이터 수신 준비
                beginListenForData();
            }catch(Exception e){
                // 블루투스 연결 중 오류 발생
                Toast.makeText(getBaseContext(), "블루투스 장치 전원을 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;      // 종료
            }
        }
   *//* void sendData(String msg){
        msg += mStrDelimiter;	// 문자열 종료 표시
        try{
            mOutputStream.write(msg.getBytes());		// 문자열 전송
        }catch(Exception e){
            // 문자열 전송 도중 오류가 발생한 경우
            finish();		// 어플리케이션 종료
        }
    }*//*

        BluetoothDevice getDeviceFromBondedList(String name){
            BluetoothDevice selectedDevice = null;

            for (BluetoothDevice device : mDevices) {
                if(name.equals(device.getName())){
                    selectedDevice = device;
                    break;
                }
            }

            return selectedDevice;
        }

        void beginListenForData() {
            final Handler handler = new Handler();

            readBuffer = new byte[1024];	// 수신 버퍼
            readBufferPosition = 0;		// 버퍼 내 수신 문자 저장 위치

            // 문자열 수신 쓰레드
            mWorkerThread = new Thread(new Runnable(){
                String cutbmi;//bmi 인트로 바구려고 자른거
                int bminum;//경고음이나메시지 이벤트 발생 시키려고 만듬
                public void run(){

                    while(!Thread.currentThread().isInterrupted()){
                        try {

                            int bytesAvailable = mInputStream.available();	// 수신 데이터 확인
                            if(bytesAvailable > 0){		// 데이터가 수신된 경우
                                byte[] packetBytes = new byte[bytesAvailable];
                                mInputStream.read(packetBytes);
                                for(int i = 0; i < bytesAvailable; i++){
                                    byte b = packetBytes[i];
                                    if(b == mCharDelimiter){
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0, encodedBytes.length); //readBuffer의0번쨰부터 encodedBytes의 0번쨰이후에 encodeBytes의 길이 만큼 넣는다.
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        //System.out.println(data);

                                        cutbmi=data.replace("B","").trim();//B를 공백문자로 바꾸고 그trim으로 공백제거해서 숫자만 구해줌.
                                        //System.out.println(cutbmi);
                                        bminum=Integer.parseInt(cutbmi);
                                        System.out.println("폰번호:"+PHnum);
                                        // System.out.println("문자 내용:"+PHtext);

                                        if(bminum>200&&alertcount<1){
                                            //permission 어쩌구 저쩌구
                                            if (ActivityCompat.checkSelfPermission(LoggedInWalk.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                    && ActivityCompat.checkSelfPermission(LoggedInWalk.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            //가장 최근의 위치를 가져와야 함
                                            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                                            try {
                                                lastLocationLat = myLocation.getLatitude(); //double 타입임 (필드로 지정) -위도임
                                                lastLocationLng  = myLocation.getLongitude(); //마찬가지로 double타입 -경도임

                                                list = geocoder.getFromLocation(
                                                        lastLocationLat, // 위도
                                                        lastLocationLng, // 경도
                                                        10); // 얻어올 값의 개수
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                                            }
                                            if (list != null) {
                                                if (list.size()==0) {
                                                    System.out.println("해당되는 주소 정보는 없습니다");
                                                    sendSMS(PHnum,PHtext);

                                                } else {
                                                    sendSMS(PHnum,PHtext+"[현재예상위치->"+list.get(0).getAddressLine(0).toString()+"]");

                                                    System.out.println(list.get(0).getAddressLine(0).toString());
                                                }
                                            }else{
                                                sendSMS(PHnum,PHtext+"[위치를 특정하지 못했습니다.");
                                            }
                                            //  System.out.println("문자 메세지를 전송 합니다.");

                                            alertcount++;
                                        }

                                        if(bminum>200){
                                            //System.out.println("비프음이 울림.");
                                            beeper();
                                        }
                                        handler.post(new Runnable(){
                                            public void run(){


                                                // 수신된 문자열 데이터에 대한 처리 작업
                                                mEditReceive.setText( data );


                                            }
                                        });
                                    }
                                    else{
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        }
                        catch (IOException ex){
                            // 데이터 수신 중 오류 발생
                            Toast.makeText(getBaseContext(), "데이터 수신 중 오류 발생", Toast.LENGTH_SHORT).show();
                            return;      // 종료
                        }
                    }
                }
            });

            mWorkerThread.start();
        }

        void beeper(){//비프음  시작
            mp.start();

        }


        private void sendSMS(String phoneNumber, String message) {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(LoggedInWalk.this, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(LoggedInWalk.this, 0, new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            broadcastReceiver=new BroadcastReceiver() {
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "알림 문자 메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            registerReceiver(broadcastReceiver, new IntentFilter(SENT));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }





        // 정보를 받아오는 스레드
        class BT_BackgroundTask extends AsyncTask<Void, Void, String> {
            // (로딩창 띄우기 작업 3/1) 로딩창을 띄우기 위해 선언해준다.
            // ProgressDialog dialog = new ProgressDialog(bluegetheart.this);

            String target;  //우리가 접속할 홈페이지 주소가 들어감

            @Override
            protected void onPreExecute() {
                target = "http://ggavi2000.cafe24.com/smsList.php?userID="+ userID;  //해당 웹 서버에 접속

                // (로딩창 띄우기 작업 3/2)
                // dialog.setMessage("로딩중");
                //dialog.show();
            }  @Override
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
                // System.out.println(result);
                try {
                    //    System.out.println("어싱크타스크가 한번 돌아감");
                    // 해당 결과(result) 응답 부분을 처리
                    JSONObject jsonObject = new JSONObject(result);

                    // response에 각각의 공지사항 리스트가 담기게 됨
                    JSONArray jsonArray = jsonObject.getJSONArray("response");  //아까 변수 이름

                    int count = 0;
                    String userID, smsNum1,numsName,smsText;

                    while (count < jsonArray.length()) {
                        // 현재 배열의 원소값을 저장
                        JSONObject object = jsonArray.getJSONObject(count);

                        // 공지사항의 Content, Name, Date에 해당하는 값을 가져와라는 뜻

                        userID = object.getString("userID");
                        smsNum1 = object.getString("smsNum1");
                        numsName = object.getString("numsName");
                        smsText = object.getString("smsText");
                        // 하나의 공지사항에 대한 객체를 만들어줌
                        //여기선 별 다른 의미 없음. 넘버를 받아서 전역변수에 저장하고 그 번호를 연락할 번호로 사용할 뿐임.
                        smsNumber = new BTSmsNumber(userID, smsNum1,numsName,smsText);

                        if(smsText.length()!=0){//smsText가 DB에서 입력 값이 없으면 길이가 0이므로 이떈 미리 정해진 텍스트가 대신 보내진다.
                            PHtext=smsText;
                        }else{
                            PHtext="심장에 무리가 갈 수 있는 수치입니다.";
                        }
                        //리스트에 추가해줌
                        // smsNumberList.add(smsNumber);
                        // adapter.notifyDataSetChanged();
                        // PHnum=smsNum1;

                        // System.out.println(smsNum1+"Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        PHnum=smsNum1;
                        count++;
                    }

                    // (로딩창 띄우기 작업 3/3)
                    // 작업이 끝나면 로딩창을 종료시킨다.
                    // dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 심박수 측정할 때 끄면 종료
    @Override
    protected void onDestroy() {
        try{
            mWorkerThread.interrupt();   // 데이터 수신 쓰레드 종료
            mInputStream.close();
            mOutputStream.close();
            mSocket.close();
        }catch(Exception e){}

        super.onDestroy();
    }

*/


    /*
    @Override
    protected void onPause() {
        super.onPause();

        if(broadcastReceiver!=null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    */
}
