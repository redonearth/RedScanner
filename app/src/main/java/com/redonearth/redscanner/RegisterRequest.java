package com.redonearth.redscanner;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://10.0.0.35/UserRegister.php"; // localhost일 경우 작동 안 함. IP나 url 입력.
//    final static private String URL = "http://10.0.0.35:8080/user-register"; // localhost일 경우 작동 안 함. IP나 url 입력.
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userEmail", userEmail);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
