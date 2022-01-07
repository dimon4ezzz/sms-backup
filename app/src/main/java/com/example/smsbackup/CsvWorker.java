package com.example.smsbackup;

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

    public void saveIntoFile(List<Map<String, String>> smsList) {
        File path = new File(externalFilesDir, FILENAME);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            String text = smsList.stream()
                    .map(getMapToStringMapper())
                    .collect(Collectors.joining("\n"));
            stream.write(text.getBytes(StandardCharsets.UTF_8));
            successfulStatusListener.accept(path.getAbsolutePath());
        } catch (IOException e) {
            unsuccessfulStatusListener.accept(path.getAbsolutePath());
        }
    }

    private Function<Map<String, String>, String> getMapToStringMapper() {
        return map -> String.join(",", map.values());
    }

}
