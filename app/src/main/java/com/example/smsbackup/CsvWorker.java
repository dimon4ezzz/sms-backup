package com.example.smsbackup;

import com.example.smsbackup.model.SmsData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvWorker {
    private static final String FILENAME = "sms_list.csv";

    private final File externalFilesDir;
    private final Consumer<String> successfulStatusListener;
    private final Consumer<String> unsuccessfulStatusListener;

    public CsvWorker(
            File externalFilesDir,
            Consumer<String> successfulStatusListener,
            Consumer<String> unsuccessfulStatusListener
    ) {
        this.externalFilesDir = externalFilesDir;
        this.successfulStatusListener = successfulStatusListener;
        this.unsuccessfulStatusListener = unsuccessfulStatusListener;
    }

    public void saveIntoFile(List<SmsData> smsList) {
        File path = new File(externalFilesDir, FILENAME);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            String text = "address,body,date,sent date,service center,type\n";
            text += smsList.stream()
                    .map(getMapToStringMapper())
                    .collect(Collectors.joining("\n"));
            stream.write(text.getBytes(StandardCharsets.UTF_8));
            successfulStatusListener.accept(path.getAbsolutePath());
        } catch (IOException e) {
            unsuccessfulStatusListener.accept(path.getAbsolutePath());
        }
    }

    private Function<SmsData, String> getMapToStringMapper() {
        return smsData -> smsData.address + "," +
                "\"" + smsData.body + "\"," +
                smsData.date + "," +
                smsData.sentDate + "," +
                smsData.serviceCenter + "," +
                smsData.type;
    }

}
