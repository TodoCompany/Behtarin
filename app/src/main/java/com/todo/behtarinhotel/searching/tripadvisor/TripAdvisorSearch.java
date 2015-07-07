package com.todo.behtarinhotel.searching.tripadvisor;


import com.todo.behtarinhotel.simpleobjects.TripSearchParams;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class TripAdvisorSearch {
    /**
     * This method has implementation in hotels view list adapter;
     */
    TripSearchCallBackListener tripSearchCallBackListener;

    /**
     * @param params - this is simple object with all needed params from showing hotel.
     */
    public void search(TripSearchParams params){
        //todo volley loading
        // onResult
        if(tripSearchCallBackListener!= null){
            tripSearchCallBackListener.onTripAdvisorResult(new Object());
        }
    }

    /**
     *
     * @param tripSearchCallBackListener - we are setting our callback listener.
     */
    public void setTripSearchCallBackListener(TripSearchCallBackListener tripSearchCallBackListener) {
        this.tripSearchCallBackListener = tripSearchCallBackListener;
    }

    public interface TripSearchCallBackListener{
        void onTripAdvisorResult(Object result);
    }
}
