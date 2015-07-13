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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    //reg info
    EditText etRegName, etRegEmail, etRegPassword;
    Button btnSignUp;
    ProgressDialog pd;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        etRegName = (EditText) findViewById(R.id.et_reg_name);
        etRegEmail = (EditText) findViewById(R.id.et_reg_email);
        etRegPassword = (EditText) findViewById(R.id.et_reg_password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {

                    //LOGIN PROCESS
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
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
            mEmailView.setError("Need your email");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Wrong email address");
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
            userLoginTask(email, password);
        }
    }

    public void attemptRegister() {
        // Reset errors.
        etRegName.setError(null);
        etRegEmail.setError(null);
        etRegPassword.setError(null);
        // Store values at the time of the login attempt.
        String name = etRegName.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a name not empty
        if (name.equals("")){
            etRegName.setError("Name cannot be empty");
            focusView = etRegName;
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
            userRegisterTask(name, email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private void userLoginTask(String email, String password) {
        AppState.userLoggedIn(new UserSO("empty_name", email, password));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void userRegisterTask(String name, String email, String password) {
        AppState.userLoggedIn(new UserSO(name, email, password));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}

