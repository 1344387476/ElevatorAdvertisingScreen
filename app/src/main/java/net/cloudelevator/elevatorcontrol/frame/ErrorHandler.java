package net.cloudelevator.elevatorcontrol.frame;

import android.content.Context;

import androidx.annotation.NonNull;

public class ErrorHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        LogWriter.logToFile("Error", "崩溃信息" + e.getMessage());
        LogWriter.logToFile("Error", "崩溃线程名称" + t.getName() + "  线程ID" + t.getId());
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            LogWriter.logToFile("Error", "Line: " + element.getLineNumber() + "  Class: " + element.getClassName()
                    + "  Method: " + element.getMethodName());
        }
        e.printStackTrace();
    }

    private static ErrorHandler instance;

    public static ErrorHandler getInstance() {
        if (instance == null){
            instance = new ErrorHandler();
        }
        return instance;
    }

    private ErrorHandler() {
    }

    public void setErrorHandle(final Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
