package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
+ * Copies the local SQLite database to external storage on app startup so the
+ * raw DB file can be moved to an offline Windows PC and forwarded without any
+ * server upload.
+ */

public class StartupDbExporter {

    private static final String TAG = "StartupDbExporter";
    private static final String DATABASE_NAME = "seatech";

    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

    public StartupDbExporter(Context context) {
        this.context = context;
    }

    public void exportDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    copyDatabaseFile();
                } catch (Exception e) {
                    Log.e(TAG, "Failed to export database", e);
                }
            }
        }).start();
    }

    private void copyDatabaseFile() throws IOException {
        File sourceDb = context.getDatabasePath(DATABASE_NAME);
        if (sourceDb == null || !sourceDb.exists()) {
            Log.w(TAG, "Database file not found: " + (sourceDb != null ? sourceDb.getAbsolutePath() : "null"));
            return;
        }

        File exportDir = new File(Environment.getExternalStorageDirectory(), "SeaTech");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            Log.e(TAG, "Failed to create export directory: " + exportDir.getAbsolutePath());
            return;
        }

        String timestamp = dateFormat.format(new Date());
        File destinationDb = new File(exportDir, "seatech_backup_" + timestamp + ".db");

        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(sourceDb).getChannel();
            output = new FileOutputStream(destinationDb).getChannel();
            output.transferFrom(input, 0, input.size());
            Log.i(TAG, "Database exported to " + destinationDb.getAbsolutePath());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
