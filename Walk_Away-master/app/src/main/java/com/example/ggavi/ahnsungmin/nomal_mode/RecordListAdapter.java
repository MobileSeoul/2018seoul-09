package com.example.ggavi.ahnsungmin.nomal_mode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.ggavi.ahnsungmin.R;

import org.json.JSONObject;

import java.util.List;

public class RecordListAdapter extends BaseAdapter {

    private String userId;
    private Context context;
    private List<Record> recordList;
    private String datetimeS;

    public RecordListAdapter(Context context, List<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) //i는 position
    {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) //i는 position
    {
        return i;
    }

    @Override
    public View getView(int i, View convertView, final ViewGroup parent) //i는 position
    {
        // 하나의 View로 만들어 줄 수 있도록 한다. ('R.layout.이름'으로 배달)
        View v = View.inflate(context, R.layout.login_logged_in_record_item, null);
        TextView pedometer = (TextView) v.findViewById(R.id.pedometerRecord);
        TextView distance = (TextView) v.findViewById(R.id.distanceRecord);
        TextView calorie = (TextView) v.findViewById(R.id.calorieRecord);
        TextView time = (TextView) v.findViewById(R.id.timeRecord);
        TextView speed = (TextView) v.findViewById(R.id.speedRecord);
        TextView date = (TextView) v.findViewById(R.id.dateRecord);
        TextView serialNum = (TextView)v.findViewById(R.id.serialNum);
        TextView progress = (TextView)v.findViewById(R.id.progress);
        TextView datetime = (TextView)v.findViewById(R.id.dateTimeRecord);
        Button del = (Button) v.findViewById(R.id.deleteEachRec);


        // 현재 리스트에 있는 값으로 넣어줄 수 있도록 한다.
        pedometer.setText(recordList.get(i).getPedometer());
        distance.setText(recordList.get(i).getDistance());
        calorie.setText(recordList.get(i).getCalorie());
        time.setText(recordList.get(i).getTime());
        speed.setText(recordList.get(i).getSpeed());
        date.setText(recordList.get(i).getDate());
        datetime.setText(recordList.get(i).getDatetime());
        datetimeS = datetime.getText().toString();

        String progressSaved = recordList.get(i).getProgress().trim();
        if(progressSaved.equals("skipped")||progressSaved.equals("")){
            progress.setText("목표를 지정하지 않았습니다.");
            progress.setBackgroundColor(Color.parseColor("#c5c5c5"));
        }else {
            if(Integer.parseInt(progressSaved)<=33){ //low
                progress.setBackgroundColor(Color.parseColor("#fc8080"));
            }else if(Integer.parseInt(progressSaved)>33&&Integer.parseInt(progressSaved)<=66){ //middle
                progress.setBackgroundColor(Color.parseColor("#fcc980"));
            }else if(Integer.parseInt(progressSaved)>66){ //high
                progress.setBackgroundColor(Color.parseColor("#80fcc9"));
            }

            progress.setText("목표 달성률: "+recordList.get(i).getProgress()+"%");
        }
        serialNum.setText(i+1+")");

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = a_LoginMainActivity.userID;
                //  Toast.makeText(parent.getContext(),datetimeS+" "+userId,Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(parent.getContext()); //here, the name of the activity class that you're writing a code in, needs to be replaced
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
                dialog.setContentView(R.layout.dialog_alert); //setting view


                //getting textviews and buttons from dialog
                TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
                Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
                Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);

                //You can change the texts on java code shown as below
                dialogTitle.setText(" 기록 삭제 ");
                dialogMessage.setText("삭제하시겠습니까?");
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
                                        new CustomConfirmDialog().showConfirmDialog(parent.getContext(),"삭제하였습니다.",false);
                                        Intent intent =  ((Activity)parent.getContext()).getIntent();
                                        intent.putExtra("userID",userId);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        ((Activity)parent.getContext()).finish();
                                        ((Activity)parent.getContext()).startActivity(intent);
                                    }

                                    // 삭제 실패
                                    else {
                                        new CustomConfirmDialog().showConfirmDialog(parent.getContext(),"삭제를 실패하였습니다.",true);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        EachRecordDelete recordDelete = new EachRecordDelete(userId,datetimeS, responseListener);  // + ""를 붙이면 문자열 형태로 바꿈
                        RequestQueue queue = Volley.newRequestQueue(parent.getContext());
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
        });
        v.setTag(recordList.get(i).getUserId());
        return v;
    }


}