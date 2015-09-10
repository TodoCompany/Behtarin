package com.todo.behtarinhotel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class LauncherActivity extends ActionBarActivity {

    private static final String API_KEY = "7tuermyqnaf66ujk2dk3rkfk";
    private static final String CID = "55505";
    String apiKey = "&apiKey=";
    String cid = "&cid=";
    String locale = "&locale=enUS";
    String customerSessionID = "&customerSessionID=1";
    String customerIpAddress = "&customerIpAddress=193.93.219.63";
    String currencyCode = "&currencyCode=USD";
    String sig = "&sig=" + AppState.getMD5EncryptedString(apiKey + "RyqEsq69" + System.currentTimeMillis() / 1000L);
    String minorRev = "&minorRev=30";
    String hotelIdList = "&hotelIdList=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.base_dark));
        }

        AppState.setupAppState(this);

        if (AppState.isUserLoggedIn()) {


            UserSO user;
            user = AppState.getLoggedUser();
            userLoginTask(user.getUsername(), user.getPassword());


        } else {
            startLoginActivity(false);
        }

    }

    public void startWorkActivity() {
        Intent in = new Intent(this, MainActivity.class);
        startActivityByIntent(in);

    }

    public void startLoginActivity(boolean autoFill) {

        Intent inLogin = new Intent(this, LoginActivity.class);
        inLogin.putExtra("autoFill", autoFill);
        inLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inLogin);
        finish();

    }

    private void startActivityByIntent(Intent in) {
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        this.finish();
    }

    private void userLoginTask(final String username, final String password) {
        String url = "http://dev.behtarinhotel.com/api/user/generate_auth_cookie/?" +
                "username=" + username +
                "&password=" + password +
                "&seconds=60";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")) {
                                JSONObject user = response.getJSONObject("user");
                                    AppState.userLoggedIn(new UserSO(user.getString("firstname"),
                                            user.getString("lastname"),
                                            user.getInt("id"),
                                            user.getString("email"),
                                            password,
                                            user.getString("username"),
                                            user.getString("key")));

                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Type listOfTestObject = new TypeToken<ArrayList<Integer>>() {
                                }.getType();
                                ArrayList<Integer> wishList = new ArrayList<>();
                                wishList = gson.fromJson(user.getString("wish_list"), listOfTestObject);
                                AppState.setWishList(wishList);
                                getBookedRooms(user.getJSONArray("rooms"));
                                startWorkActivity();
                                finish();
                            }else{
                                startLoginActivity(false);
                                Toast.makeText(getApplicationContext(),"Your login or password is incorrect",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startLoginActivity(true);
                Toast.makeText(getApplicationContext(),"Something wrong with your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
        );
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void getBookedRooms(JSONArray jsonArray) throws JSONException {
        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            BookedRoomSO bookedRoomSO = new BookedRoomSO();
            bookedRoomSO.setItineraryId(object.getInt("ItineraryID"));
            bookedRoomSO.setAdult(object.getInt("adult"));
            bookedRoomSO.setUserID(object.getInt("u_id"));
            bookedRoomSO.setLastName(object.getString("LastName"));
            bookedRoomSO.setPhotoUrl(object.getString("Photo"));
            bookedRoomSO.setConfirmationNumber(object.getInt("ConfirmationNumber"));
            bookedRoomSO.setBedType(object.getInt("BedType"));
            bookedRoomSO.setSmokingPreference(object.getString("SmokingPreference"));
            bookedRoomSO.setFirstName(object.getString("FirstName"));
            bookedRoomSO.setRoomDescription(object.getString("Description"));
            bookedRoomSO.setHotelID(object.getInt("HotelID"));
            bookedRoomSO.setArrivalDate(object.getString("StartDate"));
            bookedRoomSO.setDepartureDate(object.getString("EndDate"));

            bookedRooms.add(bookedRoomSO);
        }
            getHotelDataFromExpedia(bookedRooms);
    }

    private void getHotelDataFromExpedia(final ArrayList<BookedRoomSO> rooms){
        String hotelIDs = "";
        final int num = rooms.size();
        for (BookedRoomSO room : rooms){
            hotelIDs = hotelIDs + room.getHotelID() + ",";
        }
        String url = "http://api.ean.com/ean-services/rs/hotel/v3/list?" +
                apiKey + API_KEY +
                cid + CID +
                sig +
                customerIpAddress +
                currencyCode +
                customerSessionID +
                minorRev +
                locale +
                hotelIdList + hotelIDs
        ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "123");
                        if(num==1){
                            try {
                                JSONObject obj = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONObject("HotelSummary");
                                rooms.get(0).setHotelName(obj.getString("name"));
                                rooms.get(0).setHotelAddress(obj.getString("address1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                JSONArray arr = response.getJSONObject("HotelListResponse").getJSONObject("HotelList").getJSONArray("HotelSummary");
                                for(int i=0; i<arr.length();i++){
                                    rooms.get(i).setHotelName(arr.getJSONObject(i).getString("name"));
                                    rooms.get(i).setHotelAddress(arr.getJSONObject(i).getString("address1"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        AppState.saveBookedRoom(rooms);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }

        );
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }




}