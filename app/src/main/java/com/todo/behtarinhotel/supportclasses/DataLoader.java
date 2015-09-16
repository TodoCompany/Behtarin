package com.todo.behtarinhotel.supportclasses;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DataLoader {

    public static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    public static final String CID = "55505";

    public static final String arrivalDate = "&arrivalDate=";
    public static final String departureDate = "&departureDate=";
    public static final String city = "&destinationString=";
    public static final String hotelIdList = "&hotelIdList=";
    public static final String minStar = "&minStarRating=";
    public static final String limit = "&numberOfResults=";
    public static final String apiKey = "&apiKey=";
    public static final String cid = "&cid=";
    public static final String locale = "&locale=enUS";
    public static final String customerSessionID = "&customerSessionID=1";
    public static final String customerIpAddress = "&customerIpAddress=193.93.219.63";
    public static final String sig = "&sig=" + DataLoader.getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
    public static final String minorRev = "&minorRev=30";
    public static final String currencyCode = "&currencyCode=USD";
    public static final String itineraryId = "&itineraryId=";
    public static final String confirmationNumber = "&confirmationNumber=";
    public static final String email = "&email=";
    public static final String apiUrl = "http://dev.behtarinhotel.com/api/user/booking/";


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
                + "&includeDetails=true"
                + "&includeHotelFeeBreakdown=true"
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

    public static void makeRequest(String url, Response.Listener<JSONObject> listener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }
        );

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }

    public static void makeRequest(String url,
                                   JSONObject object,
                                   Response.Listener<JSONObject> listener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                object,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }
        );

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }

    public static void makeRequest(boolean isPostMethod,
                                    String url,
                                    JSONObject object,
                                    Response.Listener<JSONObject> listener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                object,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }
        );

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }

    public static void makeRequest(boolean isPostMethod,
                                   String url,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                listener,
                errorListener
        );

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }

    public static void makeRequest(String url,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                listener,
                errorListener
        );

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }

    public static void makePostRequest(String url,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(),
                listener,
                errorListener);

        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(AppState.getMyContext()).addToRequestQueue(jsonObjectRequest);

    }
}
