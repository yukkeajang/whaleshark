package com.togetherseatech.whaleshark.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import android.os.Binder;
import android.os.Environment;
import android.util.Log;

public class LogWrapper implements UncaughtExceptionHandler{
	private static final String TAG = "LogWrapper";
	private static final int LOG_FILE_SIZE_LIMIT = 512*1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "FileLog%g.txt";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;
    private UncaughtExceptionHandler defaultUEH;
    
    public LogWrapper() {
    	//Getting the the default exception handler
    	//that's executed when uncaught exception terminates a thread
    	this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    	
    }
    
    static {
        try {
            fileHandler = new FileHandler(Environment.getExternalStorageDirectory() 
                    +File.separator + 
                    LOG_FILE_NAME, LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, true);
            
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());
                    
                    StringBuilder ret = new StringBuilder(80);
                    ret.append(formatter.format(date));
                    ret.append(r.getMessage());
                    return ret.toString();
                }
            });
            
            logger = Logger.getLogger(LogWrapper.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            Log.d(TAG, "init success");
        } catch (IOException e) {
            Log.d(TAG, "init failure");
        }
    }

    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n", 
                    tag, Binder.getCallingPid(), msg));
        }
        Log.v(tag, msg);
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		//Write a printable representation of this Throwable
		//The StringWriter gives the lock used to synchronize access to this writer.
		final Writer stringBuffSync = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringBuffSync);
		ex.printStackTrace(printWriter);
		String stacktrace = stringBuffSync.toString();
		LogWrapper.v(TAG, "uncaughtException = "+stacktrace);
		defaultUEH.uncaughtException(thread, ex);
	}

    public static void appendLog(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddhhmmss", Locale.getDefault());
        Date date = new Date();
        String filename = "licenselog"+dateFormat.format(date)+".txt";
        File logFile = new File(Environment.getExternalStorageDirectory(), filename);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // 적절한 예외처리를 해주면됩니다.
                e.printStackTrace();
            }
        }
        try {
            //퍼포먼스를 위해 BufferedWriter를 썼고 FileWriter의 true는 파일을 이어쓸 수 있게 하기 위해서 해줬습니다.
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // 적절한 예외처리를 해주면됩니다.
            e.printStackTrace();
        }
    }
}
