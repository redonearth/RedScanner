package com.redonearth.redscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setBeepEnabled(false); // 바코드 인식 시 소리
        integrator.setOrientationLocked(false); // default가 세로 모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        integrator.setPrompt("바코드/QR코드를 사각형에 맞춰주세요!");  // 작동 안함... 왜지?
        integrator.setCaptureActivity(ScannerActivity.class);
        integrator.initiateScan();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // QR코드/바코드를 스캔한 결과 값을 가져옵니다.
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(result.getContents() + " [" + result.getFormatName() + "]")
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(ScannerContainerActivity.this, ScannerContainerActivity.class));
                        }
                    })
                    .setPositiveButton("URL 이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ScannerContainerActivity.this, WebViewActivity.class);
                            String contents = data.getStringExtra("SCAN_RESULT");
                            intent.setData(Uri.parse(contents));
                            startActivity(intent);
                        }
                    })
                    .show();

            if (result.getContents() == null) {
                // 취소됨
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
