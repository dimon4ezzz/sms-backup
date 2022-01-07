package com.example.smsbackup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBackupBtnListener();
    }

    private void setBackupBtnListener() {
        View backupBtn = findViewById(R.id.backup_sms_btn);
        backupBtn.setOnClickListener(this::onClickBackupBtn);
    }

    private void onClickBackupBtn(View view) {
        boolean ok = checkSmsPermission();
        if (!ok) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_READ_SMS);
        }
    }

    private boolean checkSmsPermission() {
        int status = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        return status == PackageManager.PERMISSION_GRANTED;
    }

}
