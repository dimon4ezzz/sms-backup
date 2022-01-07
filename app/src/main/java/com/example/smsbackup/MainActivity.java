package com.example.smsbackup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_SMS = 1;

    private SmsWorker smsWorker;
    private CsvWorker csvWorker;

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
        } else {
            saveSms();
        }
    }

    private boolean checkSmsPermission() {
        int status = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        return status == PackageManager.PERMISSION_GRANTED;
    }

    private void saveSms() {
        if (smsWorker == null) {
            smsWorker = new SmsWorker(getContentResolver());
        }
        smsWorker.saveSms();
        if (csvWorker == null) {
            csvWorker = new CsvWorker(getExternalFilesDir(null), getSuccessfulStatusListener(), getUnsuccessfulStatusListener());
        }
        csvWorker.saveIntoFile(smsWorker.getAllSmsList());
    }

    private Consumer<String> getSuccessfulStatusListener() {
        return str -> {
            String text = getResources().getString(R.string.backup_successful, str);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        };
    }

    private Consumer<String> getUnsuccessfulStatusListener() {
        return str -> {
            String text = getResources().getString(R.string.backup_unsuccessful, str);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        };
    }

}
