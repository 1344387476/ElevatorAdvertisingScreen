package net.cloudelevator.elevatorcontrol.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.cloudelevator.elevatorcontrol.Adapter.SettingListAdapter;
import net.cloudelevator.elevatorcontrol.R;
import net.cloudelevator.elevatorcontrol.Utiles.MySettings;
import net.cloudelevator.elevatorcontrol.bean.SettingsBean;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private ListView listView;
    private MySettings settings;
    private ArrayList<SettingsBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        listView = findViewById(R.id.setting_list);

        settings = new MySettings();
        String[] titles = {"电梯总楼层", "制造单位", "电梯载重", "电梯速度", "使用单位", "维保单位", "电梯编号", "应急平台"};
        String[] content = {"设置总楼层数：" + settings.getFloorNum(), "更改该电梯的制造单位", "电梯最大载重量", "电梯运行速度",
                "使用该电梯的单位", "负责此电梯的维保单位", "输入电梯的编号", "设置应急平台联系方式"};

        for (int i = 0; i < titles.length; i++) {
            data.add(new SettingsBean(titles[i], content[i]));
        }

        SettingListAdapter adapter = new SettingListAdapter(SettingActivity.this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View tipView = View.inflate(SettingActivity.this, R.layout.dialog_input, null);
                final EditText editText = tipView.findViewById(R.id.input);
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setView(tipView)
                        .setNegativeButton("取消", null);

                switch (position) {
                    case 0:
                        editText.setHint(String.valueOf(settings.getFloorNum()));
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        dialog.setTitle("输入总楼层数")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String num = editText.getText().toString();
                                        try {
                                            int i = Integer.parseInt(num);
                                            settings.setFloornum(i);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        break;
                    case 1:
                        editText.setHint(settings.getManufactrueUnit());
                        dialog.setTitle("输入电梯的制造单位")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str = editText.getText().toString();
                                        settings.setManufactureUnit(str);
                                    }
                                });
                        break;
                    case 2:
                        editText.setHint(settings.getLoad() + "/Kg");
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        dialog.setTitle("输入电梯的载重量")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        Float num = Float.parseFloat(val);
                                        settings.setLoad(num);
                                    }
                                });
                        break;
                    case 3:
                        editText.setHint(settings.getSpeed() + "M/S");
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        dialog.setTitle("输入电梯速度")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        Float num = Float.parseFloat(val);
                                        settings.setSpeed(num);
                                    }
                                });
                        break;
                    case 4:
                        editText.setHint(settings.getUseUnit());
                        dialog.setTitle("输入电梯使用单位")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        settings.setUseUnit(val);
                                    }
                                });
                        break;
                    case 5:
                        editText.setHint(settings.getMaintenanceUnit());
                        dialog.setTitle("输入电梯维保单位")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        settings.setMaintenanceUnit(val);
                                    }
                                });
                        break;
                    case 6:
                        editText.setHint(settings.getElevatorNumbre());
                        dialog.setTitle("输入电梯编号")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        settings.setElevatorNumbre(val);
                                    }
                                });
                        break;
                    case 7:
                        editText.setHint(settings.getEmergencyCall());
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        dialog.setTitle("输入电梯应急平台")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String val = editText.getText().toString();
                                        settings.setEmergencyCall(val);
                                    }
                                });
                        break;
                    default:
                        break;
                }
                dialog.create().show();
            }
        });
    }
}
