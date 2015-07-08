package com.todo.behtarinhotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.todo.behtarinhotel.adapters.MainActivityMainListAdapter;
import com.todo.behtarinhotel.payment.MyPayPall;
import com.todo.behtarinhotel.searching.GlobalSearch;
import com.todo.behtarinhotel.simpleobjects.SearchResultSO;

import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends BaseMainActivity implements GlobalSearch.GlobalSearchCallBackListener {

    MyPayPall myPayPall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();

        // PayPall call payment
//        PayPallParams payPallParams = new PayPallParams(new Product("Sample product", "45.54", Product.USD));
//        myPayPall = new MyPayPall();
//        myPayPall.makeAPayment(this, payPallParams);




        GlobalSearch globalSearch = new GlobalSearch();

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


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
