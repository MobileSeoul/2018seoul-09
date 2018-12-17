package com.example.ggavi.ahnsungmin.nomal_mode;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


// 카페24와 서버 연동 : 회원가입
public class a_RegisterRequest extends StringRequest {

    final static private String URL = "http://ggavi2000.cafe24.com/UserRegister.php";
    private Map<String, String> parameters;

    public a_RegisterRequest(String userID, String userPassword, String userGender, String userMajor, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userGender", userGender);
        parameters.put("userMajor", userMajor);
        parameters.put("userEmail", userEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
