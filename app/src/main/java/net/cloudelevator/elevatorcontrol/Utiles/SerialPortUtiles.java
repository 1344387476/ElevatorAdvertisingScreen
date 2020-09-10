package net.cloudelevator.elevatorcontrol.Utiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class SerialPortUtiles {
    private SerialPort serialPort = null;
    private InputStream mIs;
    private OutputStream mOs;
    private boolean isOpen = false;
    private Thread mThread = null;
    private SerialPortListener listener;

    public void setListener(SerialPortListener listener) {
        this.listener = listener;
    }

    public interface SerialPortListener {
        void onReceiveMsg(String str);

        void onError(Exception e);
    }

    public SerialPortUtiles(int baud) {
        try {
            serialPort = new SerialPort(new File("/dev/ttyS3"), baud, 0);

            mIs = serialPort.getFis();
            mOs = serialPort.getFos();
            isOpen = true;
            receiveSerialPort();
        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    /**
     * 接收串口数据
     */
    public void receiveSerialPort() {
        if (mThread != null) {
            return;
        }

        mThread = new Thread() {
            @Override
            public void run() {
                while (isOpen) {
                    try {
                        byte[] readData = new byte[1024];
                        if (mIs == null) {
                            return;
                        }
                        int size = mIs.read(readData);
                        if (size > 0 && isOpen){
                            String msg = new String(readData,0,size);
                            if (listener != null) {
                                listener.onReceiveMsg(msg);
                            }
                        }
                    } catch (IOException e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                }
            }
        };
        mThread.start();
    }

    /**
     * 发送串口数据
     */
    public void sendSerialPort(){
        try {
            byte[] sendData = new byte[1024];
            mOs.write(sendData);
            mOs.flush();
        } catch (IOException e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort(){
        try {
            if (mOs != null) {
                mOs.close();
            }
            if (mIs != null) {
                mIs.close();
            }
            isOpen = false;
            serialPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
