package net.cloudelevator.elevatorcontrol.frame;
import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static PrefsManager prefsManager;
    private static ErrorHandler errorHandler;

    public static MyApplication getInstance() {
        return instance;
    }

    public static PrefsManager getPrefsManager(){
        return prefsManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        prefsManager = new PrefsManager(this);
        errorHandler = ErrorHandler.getInstance();
    }
}
