package com.example.ggavi.ahnsungmin.nomal_mode;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ggavi.ahnsungmin.R;

public class CustomConfirmDialog {
    private Handler handler = new Handler();



    public void  showConfirmDialog(Context context, String message, boolean isThereConfirmButton){
        final Dialog dialog;
        dialog = new Dialog(context); //here, the name of the activity class that you're writing a code in, needs to be replaced
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //for title bars not to be appeared (타이틀 바 안보이게)
        dialog.setContentView(R.layout.dialog_alert); //setting view

        //getting textviews and buttons from dialog
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialogMessage);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButton1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButton2);


        dialogTitle.setText(" 알림 ");
        dialogMessage.setText(" "+message+" ");
        dialogButton2.setVisibility(View.GONE);
        if(isThereConfirmButton){
            dialogButton1.setText("확인");
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //your code here
                    dialog.dismiss(); //to dismiss the dialog
                }
            });
        }else{
            dialogButton1.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   dialog.dismiss();
                }
            }, 100);
        }


        dialog.setCanceledOnTouchOutside(false); //dialog won't be dismissed on outside touch
        dialog.setCancelable(false); //dialog won't be dismissed on pressed back
        dialog.show(); //show the dialog


    }
}
