package com.todo.behtarinhotel.supportclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.todo.behtarinhotel.simpleobjects.UserSO;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class AppState {

    private static final String LOG_STATUS = "LogStatus";
    //API data
    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";
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

    public static String generateUrlForHotelAvailability(int hotelIdNumber, String arrDate, String depDate) {
        String endpoint = "http://api.ean.com/ean-services/rs/hotel/v3/avail?";
        String apiKey = "&apiKey=";
        String cid = "&cid=";
        String arrivalDate = "&arrivalDate=";
        String departureDate = "&departureDate=";
        String sig = "&sig=" + getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
        String hotelId = "&hotelId=" + hotelIdNumber;

        return endpoint
                + cid + CID
                + sig
                + apiKey + API_KEY
                + hotelId
                + arrivalDate + arrDate
                + departureDate + depDate
                + "&includeRoomImages=true";
    }

    public static String getMD5EncryptedString(String encTarget) {
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }

}
