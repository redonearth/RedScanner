package com.redonearth.redscanner;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://10.0.2.2/UserLogin.php"; // localhost일 경우 작동 안 함. IP나 url 입력. 10.0.2.2도 가능!
//    final static private String URL = "http://10.0.2.2:8080/UserLogin"; // localhost일 경우 작동 안 함. IP나 url 입력. 10.0.2.2도 가능!

    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
