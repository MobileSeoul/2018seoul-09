package com.example.ggavi.ahnsungmin.nomal_mode;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AddLoggedInRecordRequest extends StringRequest{
    final static private String URL = "http://ggavi2000.cafe24.com/AddLoggedInRecord.php";
    private Map<String, String> parameters;

    public AddLoggedInRecordRequest(String userId, String pedometer, String distance, String calorie, String time, String speed, String progress, Response.Listener<String> listener) {

        super(Method.POST, URL, listener, null);
        System.out.println("addLoggedInRequest");
        parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("pedometer",pedometer);
        parameters.put("distance",distance);
        parameters.put("calorie", calorie);
        parameters.put("time",time);
        parameters.put("speed",speed);
        parameters.put("progress",progress);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
