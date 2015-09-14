package com.todo.behtarinhotel.supportclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scottyab.aescrypt.AESCrypt;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.PaymentCardSO;
import com.todo.behtarinhotel.simpleobjects.UserSO;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;

public class AppState {

    private static UserSO loggedUser;
    private static Context mContext;
    private static SharedPreferences sPrefLog;
    private static SharedPreferences.Editor logEditor;

    public static void userLoggedIn(UserSO userSO) {
        if (userSO != null) {
            logEditor = sPrefLog.edit();
            loggedUser = userSO;
            logEditor.putString("firstName", loggedUser.getFirstName());
            logEditor.putString("lastName", loggedUser.getLastName());
            logEditor.putString("email", loggedUser.getEmail());
            logEditor.putString("password", loggedUser.getPassword());
            logEditor.putString("username", loggedUser.getUsername());
            logEditor.putInt("userID", loggedUser.getUserID());
            logEditor.apply();
        }
    }

    public static UserSO getLoggedUser() {
        UserSO user = null;
        if (sPrefLog.getInt("userID", -1) != -1){
            user = new UserSO();
            user.setUserID(sPrefLog.getInt("userID", -1));
            user.setFirstName(sPrefLog.getString("firstName", " "));
            user.setLastName(sPrefLog.getString("lastName", " "));
            user.setEmail(sPrefLog.getString("email", " "));
            user.setPassword(sPrefLog.getString("password", ""));
            user.setUsername(sPrefLog.getString("username", ""));
        }

        return user;
    }

    public static void setupAppState(Context context) {
        mContext = context;
        sPrefLog = context.getSharedPreferences("behtarin", Context.MODE_PRIVATE);
    }

    public static Context getMyContext() {
        return mContext;
    }

    public static void userLoggedOut() {
        sPrefLog.edit().clear().apply();
    }

    public static boolean isUserLoggedIn() {
        return !(sPrefLog.getString("username", "").equals("") &
                sPrefLog.getString("password", "").equals(""));
    }

    public static int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }

    public static void addToWishList(Integer hotel) {

        ArrayList<Integer> wishList = getWishList();
        if (wishList == null) {
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
        values.put("userID", loggedUser.getUserID());
        values.put("hotelID", hotel);
        params.put("addWishList", values);

        JSONObject obj = new JSONObject(params);
        String str = obj.toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://dev.behtarinhotel.com/api/user/booking/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if (response.getInt("status") == 200) {
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
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        VolleySingleton.getInstance(getMyContext()).addToRequestQueue(req);

    }

    public static ArrayList<Integer> getWishList() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        if (sPrefLog.getString("wishlist", "").length() == 0) {
            return null;
        } else {
            return gson.fromJson(sPrefLog.getString("wishlist", ""), listOfTestObject);
        }
    }

    public static void clearWishlist(){
        sPrefLog.edit().remove("wishlist").apply();
    }

    public static boolean isInWishList(int hotelID) {
        boolean is = false;
        ArrayList<Integer> wishList = getWishList();
        if (wishList == null) {
            is = false;
        } else {
            for (Integer item : wishList) {
                if (item == hotelID) {
                    is = true;
                    break;
                }
            }
        }
        return is;
    }

    public static void removeFromWishList(int hotelID) {
        ArrayList<Integer> wishList = getWishList();
        if (wishList == null) return;
        for (int i = 0; i < wishList.size(); i++) {
            if (wishList.get(i) == hotelID) {
                wishList.remove(i);
                break;
            }
        }
        logEditor = sPrefLog.edit();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String wishListString;
        if (wishList.isEmpty()) {
            wishListString = "";
        } else {
            wishListString = gson.toJson(wishList);
        }
        logEditor.putString("wishlist", wishListString);
        logEditor.apply();

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> values = new HashMap<>();
        values.put("userID", loggedUser.getUserID());
        values.put("hotelID", hotelID);
        params.put("deleteWishList", values);

        JSONObject obj = new JSONObject(params);
        String str = obj.toString();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://dev.behtarinhotel.com/api/user/booking/",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response :"status":200,"success":"Yep"

                            Log.i("Response :", response.toString());

                            if (response.getInt("status") == 200) {
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
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        VolleySingleton.getInstance(getMyContext()).addToRequestQueue(req);
    }

    public static void setWishList(ArrayList<Integer> wishList) {
        logEditor = sPrefLog.edit();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String wishListString = gson.toJson(wishList);
        logEditor.putString("wishlist", wishListString);
        logEditor.apply();
    }

    public static ArrayList<BookedRoomSO> getBookedRooms() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        if (sPrefLog.getString("bookedRooms", "").length() == 0) {
            return new ArrayList<>();
        } else {
            try {
                return gson.fromJson(sPrefLog.getString("bookedRooms", ""), listOfTestObject);
            } catch (Exception notArray) {
                ArrayList<BookedRoomSO> arrayList = new ArrayList<>();
                String str = sPrefLog.getString("bookedRooms", "");
                BookedRoomSO bookedRoomSO = gson.fromJson(sPrefLog.getString("bookedRooms", ""), BookedRoomSO.class);
                arrayList.add(bookedRoomSO);
                return arrayList;
            }
        }
    }

    public static void saveBookedRoom(ArrayList<BookedRoomSO> roomsToBook) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();
        if (sPrefLog.getString("bookedRooms", "").length() == 0) {
            bookedRooms.addAll(roomsToBook);
        } else {
            bookedRooms = gson.fromJson(sPrefLog.getString("bookedRooms", ""), listOfTestObject);
            bookedRooms.addAll(roomsToBook);
        }
        sPrefLog.edit().putString("bookedRooms", gson.toJson(bookedRooms)).apply();
        addToHistory(roomsToBook);

    }

    public static void removeRoomFromBooking(BookedRoomSO roomToDelete) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();
        if (sPrefLog.getString("bookedRooms", "").length() != 0) {
            bookedRooms = gson.fromJson(sPrefLog.getString("bookedRooms", ""), listOfTestObject);
            for (int i = 0; i < bookedRooms.size(); i++) {
                if (bookedRooms.get(i).getItineraryId() == roomToDelete.getItineraryId()) {
                    bookedRooms.remove(bookedRooms.get(i));
                    changeRoomHistoryState(roomToDelete, BookedRoomSO.CANCELLED);
                    sPrefLog.edit().putString("bookedRooms", gson.toJson(bookedRooms)).apply();
                }
            }
        }
    }

    public static void clearBookedRooms(){
        sPrefLog.edit().remove("bookedRooms").apply();
    }

    public static ArrayList<BookedRoomSO> getHistory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        if (sPrefLog.getString("history", "").length() == 0) {
            return new ArrayList<>();
        } else {
            return gson.fromJson(sPrefLog.getString("history", ""), listOfTestObject);
        }
    }

    public static void addToHistory(ArrayList<BookedRoomSO> roomsToAdd) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        ArrayList<BookedRoomSO> history = new ArrayList<>();
        if (sPrefLog.getString("history", "").length() == 0) {
            history.addAll(roomsToAdd);
        } else {
            history = gson.fromJson(sPrefLog.getString("history", ""), listOfTestObject);
            history.addAll(roomsToAdd);
        }
        sPrefLog.edit().putString("history", gson.toJson(history)).apply();

    }

    public static void changeRoomHistoryState(BookedRoomSO room, int state) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<BookedRoomSO>>() {
        }.getType();
        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();
        if (sPrefLog.getString("history", "").length() != 0) {
            bookedRooms = gson.fromJson(sPrefLog.getString("history", ""), listOfTestObject);
            for (int i = 0; i < bookedRooms.size(); i++) {
                if (bookedRooms.get(i).getItineraryId() == room.getItineraryId()) {
                    bookedRooms.get(i).setOrderState(state);
                    sPrefLog.edit().putString("history", gson.toJson(bookedRooms)).apply();
                }
            }
        }
    }

    public static void clearHistory(){
        sPrefLog.edit().remove("history").apply();
    }

    public static ArrayList<PaymentCardSO> getCreditCards() throws GeneralSecurityException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Type listOfTestObject = new TypeToken<ArrayList<PaymentCardSO>>() {
        }.getType();

        if (sPrefLog.getString("paymentCards", "").length() == 0) {
            return new ArrayList<>();
        } else {
            String encrypted = sPrefLog.getString("paymentCards", "");
            String decrypted = AESCrypt.decrypt(loggedUser.getKey(), encrypted);

            return gson.fromJson(decrypted, listOfTestObject);
        }
    }

    public static void addPaymentCard(PaymentCardSO paymentCardSO) throws GeneralSecurityException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<PaymentCardSO>>() {
        }.getType();
        ArrayList<PaymentCardSO> paymentCards = new ArrayList<>();
        if (sPrefLog.getString("paymentCards", "").length() == 0) {
            paymentCards.add(paymentCardSO);
        } else {
            paymentCards = gson.fromJson(AESCrypt.decrypt(loggedUser.getKey(), sPrefLog.getString("paymentCards", "")), listOfTestObject);
            paymentCards.add(paymentCardSO);
        }
        sPrefLog.edit().putString("paymentCards", AESCrypt.encrypt(loggedUser.getKey(), gson.toJson(paymentCards))).apply();
    }

    public static void removePaymentCard(PaymentCardSO paymentCardSO) throws GeneralSecurityException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listOfTestObject = new TypeToken<ArrayList<PaymentCardSO>>() {
        }.getType();
        ArrayList<PaymentCardSO> paymentCards = new ArrayList<>();
        if (sPrefLog.getString("paymentCards", "").length() != 0) {
            paymentCards = gson.fromJson(AESCrypt.decrypt(loggedUser.getKey(), sPrefLog.getString("paymentCards", "")), listOfTestObject);
            for (int i = 0; i < paymentCards.size(); i++) {
                if (paymentCards.get(i).getCreditCardNumber().equals(paymentCardSO.getCreditCardNumber())) {
                    paymentCards.remove(paymentCards.get(i));
                    sPrefLog.edit().putString("paymentCards", AESCrypt.encrypt(loggedUser.getKey(), gson.toJson(paymentCards))).apply();
                }
            }
        }
    }

}
