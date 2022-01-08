package com.example.smsbackup.model;

import android.provider.Telephony;

public class SmsData {
    public String address;
    public String body;
    public Long date;
    public Long sentDate;
    public String serviceCenter;
    public Integer type;

    public String getStringType() {
        switch (type) {
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX: return "inbox";
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT: return "sent";
            default: return type.toString();
        }
    }
}
