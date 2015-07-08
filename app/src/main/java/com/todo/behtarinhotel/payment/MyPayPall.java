package com.todo.behtarinhotel.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.todo.behtarinhotel.simpleobjects.PayPallParams;

import java.math.BigDecimal;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class MyPayPall {

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(MyPayPallResource.CLIENT_ID);


    public void makeAPayment(Activity activity, PayPallParams params){

        startPaymentService(activity);

        PayPalPayment payment = new PayPalPayment(
                new BigDecimal(params.getProduct().getPrice()),
                params.getProduct().getCurrency(),
                params.getProduct().getProductName(),
                PayPalPayment.PAYMENT_INTENT_SALE);



        Intent intent = new Intent(activity, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        activity.startActivityForResult(intent, 0);

        stopPaymentService(activity);

    }

    private void startPaymentService(Context context){
        Intent intent = new Intent(context, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        context.startService(intent);
    }

    private void stopPaymentService(Context context){
        context.stopService(new Intent(context, PayPalService.class));
    }



}
