package com.todo.behtarinhotel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.searching.GlobalSearch;
import com.todo.behtarinhotel.simpleobjects.SearchParams;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements GlobalSearch.GlobalSearchCallBackListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GlobalSearch globalSearch = new GlobalSearch();
//
//        globalSearch.setGlobalSearchCallBackListener(this);
//
//        globalSearch.searchingHotelsByParams(new SearchParams());

        ListView listView = (ListView) findViewById(R.id.lv_main_list_main_activity);
        SearchResultSO searchResultSO = new SearchResultSO("Hostel 639",
                                                            "London",
                                                            "639 Harrow Road",
                                                            100,
                                                            3.5f,
                                                            10,
                "http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/2.0-12345-4.gif",
                                                            "Bla Bla Bla",
                "http://images.travelnow.com//hotels/3000000/2840000/2831600/2831521/2831521_59_b.jpg");

        ArrayList<SearchResultSO> searchResultSOArrayList = new ArrayList<>();
        searchResultSOArrayList.add(searchResultSO);

        listView.setAdapter(new MainActivityMainListAdapter(this, searchResultSOArrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onResult(ArrayList<Object> result) {

    }
}
