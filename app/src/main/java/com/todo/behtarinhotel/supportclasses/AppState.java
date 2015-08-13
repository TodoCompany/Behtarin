package com.todo.behtarinhotel.supportclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;
import com.todo.behtarinhotel.simpleobjects.UserSO;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class AppState {

    public static final int MY_SCAN_REQUEST_CODE = 1;

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

    public static String generateUrlForHotelAvailability(int hotelIdNumber, String arrDate, String depDate, ArrayList<SearchRoomSO> rooms) {
        String endpoint = "http://api.ean.com/ean-services/rs/hotel/v3/avail?minorRev=30";
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
                + "&includeRoomImages=true"
                +"&includeDetails=true"
                + makeRoomString(rooms);

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

    public static String makeRoomString(ArrayList<SearchRoomSO> rooms) {
        String room = "";
        for (int a = 0; a < rooms.size(); a++) {
            room = room + "&room" + (a + 1) + "=";
            for (int b = 0; b < rooms.get(a).getGuests().size(); b++) {
                if (b == rooms.get(a).getGuests().size() - 1) {
                    room = room + rooms.get(a).getGuests().get(b).getAge();
                } else {
                    room = room + rooms.get(a).getGuests().get(b).getAge() + ",";
                }
            }

        }
        return room;
    }

    public static String getHotelImagesUrl(int hotelIdNumber) {
        String url;

        String endpoint = "http://api.ean.com/ean-services/rs/hotel/v3/info?";
        String apiKey = "&apiKey=";
        String cid = "&cid=";
        String sig = "&sig=" + getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
        String hotelId = "&hotelId=" + hotelIdNumber;
        String options = "&options=HOTEL_IMAGES";

        url = endpoint
                + cid + CID
                + sig
                + apiKey + API_KEY
                + hotelId
                + options;
        return url;
    }

    public static int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = mContext.getResources().getDisplayMetrics().density;

        // Convert the dps to pixels, based on density scale

        return (int) (input * scale + 0.5f);
    }

    public static void addToWishList(Integer hotel) {

        ArrayList<Integer> wishList = getWishList();
        if(wishList == null){
            wishList = new ArrayList<>();
        }
        wishList.add(hotel);
        logEditor = sPrefLog.edit();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String wishListString = gson.toJson(wishList);
        logEditor.putString("wishlist", wishListString);
        logEditor.apply();

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> values = new HashMap<>();
        values.put("userID", 10);
        values.put("hotelID", 10);
        params.put("deleteWishList",values);

        JSONObject obj = new JSONObject(params);
        String str = obj.toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,"http://behtarinhotel.com/wp/api/user/booking/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if(response.getInt("status") == 200){


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        VolleySingleton.getInstance(getMyContext()).addToRequestQueue(req);

    }

    public static ArrayList<Integer> getWishList() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        if(sPrefLog.getString("wishlist", "").length()==0){
            return null;
        }else{
            return gson.fromJson(sPrefLog.getString("wishlist", ""), listOfTestObject);
        }
    }

    public static boolean isInWishList(int hotelID){
        boolean is = false;
        ArrayList<Integer> wishList = getWishList();
        if(wishList==null){
            is=false;
        }else{
            for(Integer item : wishList){
                if(item==hotelID){
                    is = true;
                    break;
                }
            }
        }
        return is;
    }

    public static void removeFromWishList(int hotelID){
        ArrayList<Integer> wishList = getWishList();
        for (int i = 0; i<wishList.size(); i++){
            if(wishList.get(i)==hotelID){
                wishList.remove(i);
                break;
            }
        }
        logEditor = sPrefLog.edit();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String wishListString;
        if(wishList.isEmpty()){
            wishListString = "";
        }else{
            wishListString = gson.toJson(wishList);
        }
        logEditor.putString("wishlist",wishListString);
        logEditor.apply();

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> values = new HashMap<>();
        values.put("userID", 10);

        params.put("getWishList",values);

        JSONObject obj = new JSONObject(params);
        String str = obj.toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,"http://behtarinhotel.com/wp/api/user/booking/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if(response.getInt("status") == 200){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        VolleySingleton.getInstance(getMyContext()).addToRequestQueue(req);
    }



}
