package com.todo.behtarinhotel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class LauncherActivity extends ActionBarActivity {

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
            userLoginTask(AppState.getLoggedUser().getUsername(), AppState.getLoggedUser().getPassword());
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
                                        user.getString("username")));
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Type listOfTestObject = new TypeToken<ArrayList<Integer>>() {
                                }.getType();
                                ArrayList<Integer> wishList = new ArrayList<>();
                                wishList = gson.fromJson(user.getString("wish_list"), listOfTestObject);
                                AppState.setWishList(wishList);
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
        int socketTimeout = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }


}