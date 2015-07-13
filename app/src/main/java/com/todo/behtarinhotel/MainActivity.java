package com.todo.behtarinhotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.todo.behtarinhotel.fragments.MyAccountFragment;
import com.todo.behtarinhotel.fragments.TestFragment;
import com.todo.behtarinhotel.payment.MyPayPall;
import com.todo.behtarinhotel.searching.GlobalSearch;
import com.todo.behtarinhotel.supportclasses.AppState;

import org.json.JSONException;

import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;


public class MainActivity extends BaseMainActivity implements GlobalSearch.GlobalSearchCallBackListener {

    MyPayPall myPayPall;

    @Override
    public void init(Bundle savedInstanceState) {


        this.addAccount(new MaterialAccount(
                getResources(),
                AppState.getLoggedUser().getFullName(), "",
                R.mipmap.icon_profile,
                R.drawable.back_drawer));
        addAccountSection(newSection("Profile", new MyAccountFragment()));
        this.addSection(newSection("Section 1", new TestFragment()));
        this.addSection(newSection("Section 2", new TestFragment()));
        this.addSection(newSection("Log out", new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                Intent in = new Intent(MainActivity.this, LauncherActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                AppState.userLoggedOut();
            }
        }));
        this.addSubheader("Subheader title");
        closeDrawer();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // set the indicator for child fragments
        // N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
        this.setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    @Override
    public void onHomeAsUpSelected() {
        // when the back arrow is selected this method is called

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//
//        //initDrawer();
//
//        // PayPall call payment
////        PayPallParams payPallParams = new PayPallParams(new Product("Sample product", "45.54", Product.USD));
////        myPayPall = new MyPayPall();
////        myPayPall.makeAPayment(this, payPallParams);
//
//
//
//
//        GlobalSearch globalSearch = new GlobalSearch();
//
//        ListView listView = (ListView) findViewById(R.id.lv_main_list_main_activity);
//SearchResultSO searchResultSO = new SearchResultSO("http://images.travelnow.com//hotels/3000000/2840000/2831600/2831521/2831521_59_b.jpg",
//        "Hostel 639",
//        "London",
//        "639 Harrow Road",
//        100,
//        3.5f,
//        10,
//        "http://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/2.0-12345-4.gif",
//        "Bla Bla Bla",
//        42.33f,
//        -71.111336f);

//
//        ArrayList<SearchResultSO> searchResultSOArrayList = new ArrayList<>();
//        searchResultSOArrayList.add(searchResultSO);
//
//        listView.setAdapter(new MainActivityMainListAdapter(this, searchResultSOArrayList));
//    }




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
