package com.example.ggavi.ahnsungmin.nomal_mode;

// 로그인 관련 서버세팅을 끝낸 뒤 만듬
// 해당 서버에 직접 로그인 요청을 보내는 클래스

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class a_LoginRequest extends StringRequest{

    final static private String URL = "http://ggavi2000.cafe24.com/UserLogin.php";
    private Map<String, String> parameters;

    public a_LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
