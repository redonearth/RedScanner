package com.redonearth.redscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

public class ScannerContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.setOrientationLocked(false); // default가 세로 모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        integrator.setPrompt("바코드/QR코드를 사각형에 맞춰주세요!");  // 작동 안함... 왜지?
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // QR코드/바코드를 스캔한 결과 값을 가져옵니다.
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result.getContents() == null) {
                // 취소됨
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                // 스캔된 QR Code --> result.getContents()
                Toast.makeText(this, "스캔: " + result.getContents(), Toast.LENGTH_LONG).show();
                String contents = data.getStringExtra("SCAN_RESULT");
                if (Pattern.matches("[0-9]{1,13}", contents)) {

                } else {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.setData(Uri.parse(contents));
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
