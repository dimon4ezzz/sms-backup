package com.example.smsbackup;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.example.smsbackup.model.SmsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsWorker {
    private final ContentResolver contentResolver;

    private final String[] smsProjection;
    private final List<SmsData> allSmsList;

    public SmsWorker(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.smsProjection = new String[]{
                Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.BODY,
                Telephony.TextBasedSmsColumns.DATE,
                Telephony.TextBasedSmsColumns.DATE_SENT,
                Telephony.TextBasedSmsColumns.SERVICE_CENTER,
                Telephony.TextBasedSmsColumns.TYPE
        };
        this.allSmsList = new ArrayList<>();
    }

    public void saveSms() {
        allSmsList.clear();
        saveInbox();
        saveSent();
    }

    public List<SmsData> getAllSmsList() {
        return allSmsList;
    }

    private void saveInbox() {
        saveSmsList(Telephony.Sms.CONTENT_URI);
    }

    private void saveSent() {
        saveSmsList(Telephony.Sms.Sent.CONTENT_URI);
    }

    private void saveSmsList(Uri contentUri) {
        Cursor cursor = contentResolver.query(contentUri, smsProjection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SmsData smsData = new SmsData();
                int idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS);
                smsData.address = cursor.getString(idx);
                idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
                smsData.body = cursor.getString(idx);
                idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                smsData.date = cursor.getLong(idx);
                idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE_SENT);
                smsData.sentDate = cursor.getLong(idx);
                idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SERVICE_CENTER);
                smsData.serviceCenter = cursor.getString(idx);
                idx = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.TYPE);
                smsData.type = cursor.getInt(idx);
                allSmsList.add(smsData);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}
