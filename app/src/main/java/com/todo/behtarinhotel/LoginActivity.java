package com.todo.behtarinhotel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;
import com.todo.behtarinhotel.supportclasses.DataLoader;
import com.todo.behtarinhotel.supportclasses.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    //reg info
    MaterialEditText etRegUserName, etRegEmail, etRegPassword, etRegFirstName, etRegLastName, etRegConfirmPassword;
    ButtonRectangle btnSignUp, mEmailSignInButton;

    // UI references.
    private MaterialEditText mEmailView;
    private MaterialEditText mPasswordView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (MaterialEditText) findViewById(R.id.email);
        etRegUserName = (MaterialEditText) findViewById(R.id.et_reg_username);
        etRegEmail = (MaterialEditText) findViewById(R.id.et_reg_email);
        etRegPassword = (MaterialEditText) findViewById(R.id.et_reg_password);
        etRegConfirmPassword = (MaterialEditText) findViewById(R.id.et_reg_confirm_password);
        etRegFirstName = (MaterialEditText) findViewById(R.id.et_reg_first_name);
        etRegLastName = (MaterialEditText) findViewById(R.id.et_reg_last_name);
        btnSignUp = (ButtonRectangle) findViewById(R.id.btn_sign_up);

        mPasswordView = (MaterialEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    //LOGIN PROCESS
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mEmailSignInButton = (ButtonRectangle) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        if (getIntent().getBooleanExtra("autoFill", false)) {


            UserSO user = AppState.getLoggedUser();
            mEmailView.setText(user.getUsername());
            mPasswordView.setText(user.getPassword());


        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Invalid password");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Need your email or username");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            userLoginTask(email, password);
        }
    }

    public void attemptRegister() {
        // Reset errors.
        etRegUserName.setError(null);
        etRegEmail.setError(null);
        etRegPassword.setError(null);
        etRegConfirmPassword.setError(null);
        // Store values at the time of the login attempt.
        final String userName = etRegUserName.getText().toString();
        final String email = etRegEmail.getText().toString();
        final String password = etRegPassword.getText().toString();
        String confirmPassword = etRegConfirmPassword.getText().toString();
        final String firstName = etRegFirstName.getText().toString();
        final String lastName = etRegLastName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a name not empty
        if (userName.equals("")) {
            etRegUserName.setError("Name can not be empty");
            focusView = etRegUserName;
            cancel = true;
        }

        if (!password.equals(confirmPassword)) {
            etRegConfirmPassword.setError("Passwords don`t match");
            focusView = etRegConfirmPassword;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etRegPassword.setError("Invalid password");
            focusView = etRegPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Need your email");
            focusView = etRegEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etRegEmail.setError("Wrong email address");
            focusView = etRegEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        String nonce = response.getString("nonce");
                        userRegisterTask(userName, firstName, lastName, email, password, nonce);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something wrong with your internet connection", Toast.LENGTH_SHORT).show();
                }
            };

            String url = "http://dev.behtarinhotel.com/api/get_nonce/?controller=user&method=register";
            DataLoader.makeRequest(url, listener, errorListener);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private void userLoginTask(final String username, final String password) {
        String url = "http://dev.behtarinhotel.com/api/user/generate_auth_cookie/?" +
                "username=" + username +
                "&password=" + password +
                "&seconds=60";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
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

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something wrong with your internet connection", Toast.LENGTH_SHORT).show();
            }
        };

        DataLoader.makeRequest(url, listener, errorListener);
    }

    private void userRegisterTask(final String userName, String firstName, String lastName, final String email, final String password, String nonce) {

        String url = "http://dev.behtarinhotel.com/api/user/register/?" +
                "username=" + userName +
                "&email=" + email +
                "&user_pass=" + password +
                "&display_name=" + userName +
                "&nonce=" + nonce +
                "&first_name=" + firstName +
                "&last_name=" + lastName;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("status").equals("ok")) {
                        userLoginTask(userName, password);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Something wrong with your internet connection", Toast.LENGTH_SHORT).show();
            }
        };

        DataLoader.makeRequest(url, listener, errorListener);
    }
}

