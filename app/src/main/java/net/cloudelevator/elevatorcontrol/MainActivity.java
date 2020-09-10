package net.cloudelevator.elevatorcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import net.cloudelevator.elevatorcontrol.Activity.SettingActivity;
import net.cloudelevator.elevatorcontrol.Adapter.BtnListAdapter;
import net.cloudelevator.elevatorcontrol.Utiles.MySettings;
import net.cloudelevator.elevatorcontrol.Utiles.SerialPortUtiles;
import net.cloudelevator.elevatorcontrol.view.AdPresentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQ_ID = 22;
    private static final String TAG = "测试";
    private static final int OVERLAY_PERMISSION_REQ_CODE = 212;
    DisplayManager mDisplayManager;//屏幕管理类
    Display mDisplay[];//屏幕数组
    AdPresentation mPresentation;
    private RecyclerView list;
    private ArrayList<String> mData = new ArrayList<>();
    private SerialPortUtiles mSerial;
    private MySettings settings;
    private BtnListAdapter btnListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            mDisplay = mDisplayManager.getDisplays();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                showAdvDisplay();
            }
        }
        settings = new MySettings();
        initData();
        initList();
        mSerial = new SerialPortUtiles(9700);
        mSerial.setListener(new SerialPortUtiles.SerialPortListener() {
            @Override
            public void onReceiveMsg(String str) {
                mPresentation.setEleNum(str);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: "+e);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresentation.refreshText();
        initData();
        btnListAdapter.notifyDataSetChanged();
    }

    private void initData() {
        if (mData != null) {
            mData.clear();
        }
        for (int i = 1; i <= settings.getFloorNum(); i++) {
            mData.add(i+"F");
        }
    }

    private void initList() {
        list = findViewById(R.id.list);
        btnListAdapter = new BtnListAdapter(mData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,5);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(btnListAdapter);
        btnListAdapter.setListener(new BtnListAdapter.RecyclerOnClickListener() {
            @Override
            public void onClickBtn(View v, int position) {
                Toast.makeText(MainActivity.this, position+"楼", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    showAdvDisplay();
                }
            }
        }
    }

    private void showPresentation(final Presentation presentation) {
        presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        presentation.show();
    }

    private void showAdvDisplay() {
        if (mDisplay.length < 2) {
            return;
        }
        mPresentation = new AdPresentation(getApplicationContext(), mDisplay[1]);
        showPresentation(mPresentation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 93) {
            Map<String, String> map = new HashMap<>();
            map.put("class", "AdPresentation");
            mPresentation.onEvent(map);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void settingClick(View view) {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

}