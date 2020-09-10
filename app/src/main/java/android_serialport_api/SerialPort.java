package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPort {
    private static final String TAG = "串口";
    private FileDescriptor mFd;
    private FileInputStream fis;
    private FileOutputStream fos;

    public SerialPort(File device, int baud, int flag) throws IOException {

        if (!device.canRead() || !device.canWrite()) {
            try {
                Process su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         mFd = open(device.getAbsolutePath(), baud, flag);

        if (mFd == null) {
            Log.e(TAG, "native open returns null ");
            throw new IOException();
        }

        fis = new FileInputStream(mFd);
        fos = new FileOutputStream(mFd);
    }


    // JNI(调用java本地接口，实现串口的打开和关闭)
/**串口有五个重要的参数：串口设备名，波特率，检验位，数据位，停止位
 其中检验位一般默认位NONE,数据位一般默认为8，停止位默认为1*/
    /**
     * @param path     串口设备的据对路径
     * @param baudrate 波特率
     * @param flags    校验位
     */
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {//加载jni下的C文件库
        System.loadLibrary("serial_port");
    }

    public FileInputStream getFis() {
        return fis;
    }

    public FileOutputStream getFos() {
        return fos;
    }
}
