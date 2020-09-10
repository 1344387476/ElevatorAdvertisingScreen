package net.cloudelevator.elevatorcontrol.Utiles;
import net.cloudelevator.elevatorcontrol.frame.MyApplication;
import net.cloudelevator.elevatorcontrol.frame.PrefsManager;

public class MySettings {
    public static final String FLOOR_NUM = "FloorNum";
    public static final String MANUFACTRUE_UNIT = "ManufactureUnit";
    public static final String LOAD = "Load";
    public static final String SPEED = "Speed";
    public static final String USE_UNIT = "UseUnit";
    public static final String MAINTENANCE_UNIT = "MaintenanceUnit";
    public static final String ELEVATOR_NUMBRE = "ElevatorNumber";
    public static final String EMERGENCY_CALL = "EmergencyCall";
    public static final String DEFAULT_STRING = "未知";

    private PrefsManager prefsManager;

    public MySettings(){
        this.prefsManager = MyApplication.getPrefsManager();
    }

    public int getFloorNum(){
        int floorNum = prefsManager.getInt(FLOOR_NUM);
        if (floorNum == 0){
            return 30;
        }
        return floorNum;
    }

    public void setFloornum(int num){
        prefsManager.putInt(FLOOR_NUM,num);
    }

    public void setManufactureUnit(String name){
        prefsManager.putString(MANUFACTRUE_UNIT,name);
    }

    public String getManufactrueUnit(){
        String name = prefsManager.getString(MANUFACTRUE_UNIT);
        if (name == null){
            return DEFAULT_STRING;
        }
        return name;
    }

    public void setLoad(Float num){
        prefsManager.putFloat(LOAD,num);
    }

    public Float getLoad(){
        Float num = prefsManager.getFloat(LOAD);
        if (num == 0.0f){
            return 1050f;
        }
        return num;
    }

    public void setSpeed(Float speed){
        prefsManager.putFloat(SPEED,speed);
    }

    public Float getSpeed(){
        Float speed = prefsManager.getFloat(SPEED);
        if (speed == 0.0f){
            return 2.50f;
        }
        return speed;
    }

    public void setMaintenanceUnit(String name){
        prefsManager.putString(MAINTENANCE_UNIT,name);
    }

    public String getMaintenanceUnit(){
        String name = prefsManager.getString(MAINTENANCE_UNIT);
        if (name == null){
            return DEFAULT_STRING;
        }
        return name;
    }

    public void setElevatorNumbre(String num){
        prefsManager.putString(ELEVATOR_NUMBRE,num);
    }

    public String getElevatorNumbre(){
        String name = prefsManager.getString(ELEVATOR_NUMBRE);
        if (name == null){
            return DEFAULT_STRING;
        }
        return name;
    }

    public void setUseUnit(String name){
        prefsManager.putString(USE_UNIT,name);
    }

    public String getUseUnit(){
        String name = prefsManager.getString(USE_UNIT);
        if (name == null){
            return DEFAULT_STRING;
        }
        return name;
    }

    public void setEmergencyCall(String num){
        prefsManager.putString(EMERGENCY_CALL,num);
    }

    public String getEmergencyCall(){
        String num = prefsManager.getString(EMERGENCY_CALL);
        if (num == null){
            return DEFAULT_STRING;
        }
        return num;
    }
}
