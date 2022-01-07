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
    private final Consumer<String> statusListener;

    public CsvWorker(File externalFilesDir, Consumer<String> statusListener) {
        this.externalFilesDir = externalFilesDir;
        this.statusListener = statusListener;
    }

    public void saveIntoFile(List<Map<String, String>> smsList) {
        File path = new File(externalFilesDir, FILENAME);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            String text = smsList.stream()
                    .map(getMapToStringMapper())
                    .collect(Collectors.joining("\n"));
            stream.write(text.getBytes(StandardCharsets.UTF_8));
            statusListener.accept("placed in: " + path.getAbsolutePath());
        } catch (IOException e) {
            statusListener.accept("cannot save CSV file in: " + path.getAbsolutePath());
        }
    }

    private Function<Map<String, String>, String> getMapToStringMapper() {
        return map -> String.join(",", map.values());
    }

}
