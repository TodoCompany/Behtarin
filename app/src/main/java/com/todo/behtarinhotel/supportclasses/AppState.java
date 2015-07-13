package com.todo.behtarinhotel.supportclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.todo.behtarinhotel.simpleobjects.UserSO;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class AppState {

    private static final String LOG_STATUS = "LogStatus";
    public static int screenWidth;
    public static int screenHeight;
    private static UserSO loggedUser;
    private static Context mContext;
    private static SharedPreferences sPrefLog;
    private static SharedPreferences.Editor logEditor;
    private static boolean isTablet;

    public static void userLoggedIn(UserSO userSO) {
        if (userSO != null) {
            logEditor = sPrefLog.edit();
            loggedUser = userSO;
            logEditor.putBoolean(LOG_STATUS, true);
            logEditor.putString("fullName", loggedUser.getFullName());
            logEditor.putString("email", loggedUser.getEmail());
            logEditor.putString("password", loggedUser.getPassword());

            logEditor.apply();
        }
    }

    public static UserSO getLoggedUser() {
        UserSO user = new UserSO();
        user.setFullName(sPrefLog.getString("fullName", " "));
        user.setEmail(sPrefLog.getString("email", " "));
        user.setPassword(sPrefLog.getString("password", ""));
        return user;
    }

    public static void setupAppState(Context context) {
        mContext = context;
        sPrefLog = context.getSharedPreferences("behtarin", Context.MODE_PRIVATE);
    }

    public static void setScreenSize(int w, int h) {
        screenWidth = w;
        screenHeight = h;
    }

    public static boolean isTablet() {
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    public static Context getMyContext() {
        return mContext;
    }

    public static void userLoggedOut() {
        logEditor = sPrefLog.edit();
        logEditor.putBoolean(LOG_STATUS, false);
        logEditor.apply();
    }

    public static boolean isUserLoggedIn() {
        return sPrefLog.getBoolean(LOG_STATUS, false);
    }

}
