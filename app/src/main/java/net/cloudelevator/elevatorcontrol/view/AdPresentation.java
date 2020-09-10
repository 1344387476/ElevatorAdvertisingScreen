package net.cloudelevator.elevatorcontrol.view;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import net.cloudelevator.elevatorcontrol.R;
import net.cloudelevator.elevatorcontrol.Utiles.MySettings;

import java.text.SimpleDateFormat;
import java.util.Map;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


@SuppressLint("NewApi")
public class AdPresentation extends Presentation {

    private TextView eleNum;
    private RtcEngine mRtcEngine;
    private RelativeLayout mRemoteContainer;
    private FrameLayout mLocalContainer;
    private RelativeLayout call_all;

    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;
    private AudioManager mAudioManager;
    private int streamMaxVolume;
    private  long startDate=0;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss");
    private boolean  isjietong = false;
    private TextView manufactureUnit,load,speed, useUnit,maintenanceUnit,elevatorNumber,emergencyCall;
    private MySettings settings;

    public AdPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_ad);
        settings = new MySettings();
        initUI();
        refreshText();
        initEngineAndJoinChannel();
        mAudioManager = (AudioManager) getContext().getSystemService(getContext().AUDIO_SERVICE);
        streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 3:
                    if (!isjietong) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    } else {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    }
                    handler.sendEmptyMessageDelayed(3, 1000);
                    break;
            }
        }
    };





    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    isjietong = true;
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }
    };


    @Override
    public void dismiss() {
        removeLocalVideo();
        RtcEngine.destroy();
        super.dismiss();

    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        Log.d("测试", "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.d("测试", "onTouchEvent: ");
        return super.onTouchEvent(event);
    }

    public void onEvent(Map map) {
        if (startDate==0){
            startCall();
        }
        else if (System.currentTimeMillis()-startDate>1000){
            startCall();
        }
        startDate=System.currentTimeMillis();
    }



    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        call_all = findViewById(R.id.call_all);
        eleNum = findViewById(R.id.floorNum);

        manufactureUnit = findViewById(R.id.manufactureUnit);
        load = findViewById(R.id.load);
        speed = findViewById(R.id.speed);
        useUnit = findViewById(R.id.useUnit);
        maintenanceUnit = findViewById(R.id.maintenanceUnit);
        elevatorNumber = findViewById(R.id.elevatorNumber);
        emergencyCall = findViewById(R.id.emergencyCall);

    }

    public void refreshText(){
        manufactureUnit.setText(settings.getManufactrueUnit());
        load.setText(settings.getLoad()+"Kg");
        speed.setText(settings.getSpeed()+"M/S");
        useUnit.setText(settings.getUseUnit());
        maintenanceUnit.setText(settings.getMaintenanceUnit());
        elevatorNumber.setText(settings.getElevatorNumbre());
        emergencyCall.setText(settings.getEmergencyCall());
    }

    public void setEleNum(String s){
        eleNum.setText(s);
    }



    private void startCall() {
        findViewById(R.id.remote_video_view_container).setVisibility(View.VISIBLE);
        findViewById(R.id.local_video_view_container).setVisibility(View.VISIBLE);
        call_all.setVisibility(View.VISIBLE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        mLocalView.setVisibility(View.VISIBLE);
        joinChannel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 40);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isjietong) {
                                endCall();
                                Toast.makeText(getContext(), "无人接听，请稍后再拨！", Toast.LENGTH_SHORT).show();
                            }
                            isjietong = false;
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void endCall() {
        removeRemoteVideo();
        leaveChannel();
        findViewById(R.id.remote_video_view_container).setVisibility(View.INVISIBLE);
        findViewById(R.id.local_video_view_container).setVisibility(View.INVISIBLE);
        mLocalView.setVisibility(View.INVISIBLE);
        call_all.setVisibility(View.GONE);
    }

    private void leaveChannel() {
        if (mRtcEngine == null) {
            return;
        }
        mRtcEngine.leaveChannel();
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getContext(), getContext().getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        mRtcEngine.enableVideo();
        mRtcEngine.enableAudio();
        mRtcEngine.adjustRecordingSignalVolume(100);
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        mLocalView = RtcEngine.CreateRendererView(getContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        mLocalView.setVisibility(View.INVISIBLE);
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    private void joinChannel() {
        String token = null;
        mRtcEngine.joinChannel(token, "000", "Extra Optional Data", 0);
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        mRemoteView = null;
    }

    private void onRemoteUserLeft() {
        endCall();
    }

    private void setupRemoteVideo(int uid) {
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }
        if (view != null) {
            return;
        }
        mRemoteView = RtcEngine.CreateRendererView(getContext());
        mRemoteContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
        mLocalContainer.setVisibility(View.INVISIBLE);
        mLocalView.setVisibility(View.INVISIBLE);
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }
}

