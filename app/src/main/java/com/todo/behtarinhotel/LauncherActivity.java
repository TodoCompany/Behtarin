package com.todo.behtarinhotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.todo.behtarinhotel.supportclasses.AppState;


public class LauncherActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        AppState.setupAppState(this);

        if (AppState.isUserLoggedIn()) {
            startWorkActivity();
        } else {
            startLoginActivity();
        }

    }

    public void startWorkActivity() {
        Intent in = new Intent(this, MainActivity.class);
        startActivityByIntent(in);

    }

    public void startLoginActivity() {

        Intent inLogin = new Intent(this, LoginActivity.class);
        inLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inLogin);
        finish();

    }

    private void startActivityByIntent(Intent in) {
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        this.finish();
    }


}