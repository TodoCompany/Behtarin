package com.todo.behtarinhotel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DataLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class LauncherActivity extends AppCompatActivity {

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

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
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
                        ArrayList<Integer> wishList;
                        wishList = gson.fromJson(user.getString("wish_list"), listOfTestObject);
                        AppState.setWishList(wishList);
                        AppState.clearBookedRooms();
                        AppState.clearHistory();
                        JSONArray arr = user.getJSONArray("history");
                        AppState.setBookedRooms(getRoomsFromJson(arr, false));
                        AppState.addToHistory(getRoomsFromJson(arr, true));
                        startWorkActivity();
                        finish();
                    } else {
                        startLoginActivity(false);
                        Toast.makeText(getApplicationContext(), "Your login or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startLoginActivity(true);
                Toast.makeText(getApplicationContext(), "Something wrong with your internet connection", Toast.LENGTH_SHORT).show();
            }
        };

        DataLoader.makeRequest(url, listener, errorListener);
    }

    private ArrayList<BookedRoomSO> getRoomsFromJson(JSONArray jsonArray, boolean isHistory) throws JSONException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("mm/dd/yyyy");
        ArrayList<BookedRoomSO> bookedRooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (isHistory || object.getInt("Status") == 1) {
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
                bookedRoomSO.setRoomDescription(object.getString("RoomName"));
                bookedRoomSO.setHotelID(object.getInt("HotelID"));
                bookedRoomSO.setArrivalDate(sdfDate.format(Long.valueOf(object.getString("StartDate")) * 1000));
                bookedRoomSO.setDepartureDate(sdfDate.format(Long.valueOf(object.getString("EndDate")) * 1000));
                JSONObject obj = new JSONObject(object.getString("PricesArray"));
                bookedRoomSO.setRoomPrice((float) obj.getDouble("@averageRate"));
                bookedRoomSO.setHotelName(object.getString("hotelName"));
                bookedRoomSO.setHotelAddress(object.getString("hotelAddress"));
                switch (object.getInt("Status")) {
                    case 0:
                        bookedRoomSO.setOrderState(BookedRoomSO.CANCELLED);
                        break;
                    case 1:
                        bookedRoomSO.setOrderState(BookedRoomSO.BOOKED);
                        break;
                    default:
                        bookedRoomSO.setOrderState(BookedRoomSO.BOOKED);
                        break;
                }
                bookedRooms.add(bookedRoomSO);
            }
        }
        return bookedRooms;
    }


}