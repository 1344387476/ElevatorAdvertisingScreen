package net.cloudelevator.elevatorcontrol.frame;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {
    private static final String TAG = "ElevatorControl";
    private static boolean isDebug = true;
    private static boolean isWriterTolog = false;

    /**
     * 写入日志到文件
     * @param tag
     * @param logText
     */
    public static void logToFile(String tag,String logText){
        if (!isWriterTolog){
            return;
        }
        final String needWriterMsg = tag +" "+logText;
        final String fileName =Environment.getExternalStorageDirectory().getPath()
                +"/LogFile.txt";

        final File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bf = new BufferedWriter(fileWriter);
            bf.write(needWriterMsg);
            bf.newLine();
            bf.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void d(final String msg){
        if (isDebug){
            Log.d(LogWriter.TAG,msg);
        }

        if (isWriterTolog){
            logToFile(LogWriter.TAG,msg);
        }
    }

    public static void e(final String msg){
        if (isDebug){
            Log.e(LogWriter.TAG,msg);
        }

        if (isWriterTolog){
            logToFile(LogWriter.TAG,msg);
        }
    }
}
