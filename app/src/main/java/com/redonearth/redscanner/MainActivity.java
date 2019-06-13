package com.redonearth.redscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        integrator.setPrompt("바코드/QR코드를 사각형에 맞춰주세요!");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "스캔: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
//        Log.d("onActivityResult", "onActivityResult: .");
//        if (resultCode == Activity.RESULT_OK) {
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//            String re = result.getContents();
//            String message = re;
//            Log.d("onActivityResult", "onActivityResult: ." + re);
//            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
//        }
    }
}
