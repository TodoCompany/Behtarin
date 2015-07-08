package com.todo.behtarinhotel;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;


public class SearchActivity extends Activity {

    TextView tvCheckIn;
    TextView tvCheckOut;

    EditText etLocation;
    EditText etRoom;
    EditText etAdult;
    EditText etChildren;
    RangeBar rbStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViewsById();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViewsById(){

        tvCheckIn = (TextView) findViewById(R.id.tv_check_in_search_activity);
        tvCheckOut = (TextView) findViewById(R.id.tv_check_out_search_activity);

        etLocation = (EditText) findViewById(R.id.et_location_search_activity);
        etRoom = (EditText) findViewById(R.id.et_room_search_activity);
        etAdult = (EditText) findViewById(R.id.et_adult_search_activity);
        etChildren = (EditText) findViewById(R.id.et_children_search_activity);

        rbStars = (RangeBar) findViewById(R.id.rb_stars);
        rbStars.setTickCount(5);


    }
}
