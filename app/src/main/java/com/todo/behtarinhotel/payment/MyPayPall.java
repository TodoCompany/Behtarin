package com.todo.behtarinhotel.payment;

import com.todo.behtarinhotel.simpleobjects.PayPallParams;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class MyPayPall {

    private OnPayPallPaymentListener onPayPallPaymentListener;

    public void makeAPayment(PayPallParams params){
        //todo make a pay transfer and call result listener

        if(onPayPallPaymentListener!=null){
            onPayPallPaymentListener.onPaymentSuccess("successExmpl");
            onPayPallPaymentListener.onPaymentError("errorExmpl");
        }
    }

    public void setOnPayPallPaymentListener(OnPayPallPaymentListener onPayPallPaymentListener) {
        this.onPayPallPaymentListener = onPayPallPaymentListener;
    }

    public interface OnPayPallPaymentListener{

        void onPaymentSuccess(String result);

        void onPaymentError(String error);

    }
}
