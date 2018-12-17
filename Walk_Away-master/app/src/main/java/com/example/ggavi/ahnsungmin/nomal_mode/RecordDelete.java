package com.example.ggavi.ahnsungmin.nomal_mode;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecordDelete extends StringRequest{
    final static private String URL = "http://ggavi2000.cafe24.com/deleteAllRec.php";
    private Map<String, String> parameters;

    public RecordDelete(String userID,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
