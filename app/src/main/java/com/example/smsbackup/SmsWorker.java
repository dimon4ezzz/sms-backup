package com.example.smsbackup;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsWorker {
    private final ContentResolver contentResolver;

    private final String[] smsProjection;
    private final List<Map<String, String>> allSmsList;

    public SmsWorker(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.smsProjection = new String[]{
                Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.BODY,
                Telephony.TextBasedSmsColumns.DATE,
                Telephony.TextBasedSmsColumns.DATE_SENT,
                Telephony.TextBasedSmsColumns.SERVICE_CENTER
        };
        this.allSmsList = new ArrayList<>();
    }

    public void saveSms() {
        saveInbox();
        saveSent();
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
                Map<String, String> msgData = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    msgData.put(cursor.getColumnName(i), cursor.getString(i));
                }
                allSmsList.add(msgData);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}
