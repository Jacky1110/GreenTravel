package com.jotangi.jotangi2022;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 嘉旭 on 2016/8/5.
 */
public class Logger {
    public static final int LEVEL_DEBUG = 0;
    public static final int LEVEL_INFO = 1;
    public static final int LEVEL_WARNING = 2;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_FATAL = 4;
    public static final int LEVEL_NONE = 5;
    private int mLevel;
    private Context context;
    private String mFilePre;

    public Logger(Context context, String mFilePre) {
        this.context = context;
        this.mFilePre = mFilePre;
        initLevel();
    }

    private void initLevel() {
        mLevel = Logger.LEVEL_DEBUG;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public String ExceptionStackToString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        //sw.flush();
        //pw.flush();
        String msg = sw.toString();
        sb.append(msg);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            sb.append(sw.toString());
            cause = cause.getCause();
        }
        try {
            sw.close();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        pw.close();
        return sb.toString();
    }

    private String stackToString(Throwable e) {
        StringBuilder sbReport = new StringBuilder();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            for (StackTraceElement trace : stackTraceElements) {
                sbReport.append("at: " + trace + System.lineSeparator());
            }
        }
        sbReport.append(System.lineSeparator());
        return sbReport.toString();
    }

    public void writeStack(Exception ex) {
        writeLog(LEVEL_ERROR, ex.toString());
        writeLog(LEVEL_ERROR, stackToString(ex));
        /*
        ex.printStackTrace();
        String msg = ExceptionStackToString(ex);
        writeLog(LEVEL_ERROR, msg);
        */
    }

    synchronized public void writeLog(int level, String msg) {
        Log.d(mFilePre, ""+msg);
        if (level < mLevel) {
            return;
        }

        String levelTag;
        switch (level) {
            case LEVEL_DEBUG:
                levelTag = "[D] ";
                break;
            case LEVEL_INFO:
                levelTag = "[I] ";
                break;
            case LEVEL_WARNING:
                levelTag = "[W] ";
                break;
            case LEVEL_ERROR:
                levelTag = "[E] ";
                break;
            case LEVEL_FATAL:
                levelTag = "[F] ";
                break;
            default:
                levelTag = "[N] ";
                break;
        }

        File logDir = FileUtils.getProjLogFolder(context);
        if (logDir == null) {
            return;
        }

        String today = DateTimeUtils.getCurrentTimeString(DateTimeUtils.DATEONLY);
        File dateDir = FileUtils.createSubFolder(logDir, today);
        if (dateDir == null) return;

        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
        String fileName = mFilePre + today + "_log.txt";
        File logFile = new File(dateDir, fileName);

        FileOutputStream tmpOutStream;
        String data = timeStamp + "  " + levelTag + msg + "\r\n";
        try {
            if (!logFile.exists()) {
                if (!logFile.createNewFile()) {
                    Log.d(mFilePre, "Create File: " + logFile.getAbsolutePath() + " error");
                    return;
                }
            }
            if (logFile.exists()) {
                tmpOutStream = new FileOutputStream(logFile, true);
                tmpOutStream.write(data.getBytes());
                tmpOutStream.close();
            }
        } catch (Exception e) {
            Log.d(mFilePre, "File Error:" + ""+e);
        }
    }
}
