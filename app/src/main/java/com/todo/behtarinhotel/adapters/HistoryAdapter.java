package com.todo.behtarinhotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gc.materialdesign.views.ButtonFlat;
import com.todo.behtarinhotel.R;
import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.supportclasses.AppState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoryAdapter extends BaseAdapter {

    ArrayList<BookedRoomSO> history;
    Context context;
    LayoutInflater inflater;
    RequestListener listener;

    public HistoryAdapter(final Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        history = AppState.getHistory();

        Collections.sort(history, new Comparator<BookedRoomSO>() {
            @Override
            public int compare(BookedRoomSO lhs, BookedRoomSO rhs) {

                return lhs.getOrderState() - rhs.getOrderState();
            }
        });
        listener = new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                String str = model.toString();
                Glide.with(context)
                        .load(str.substring(0, str.length() - 5) + "b.jpg")
                        .into(target);
                return true;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        };
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.history_item, null, false);
        }
        TextView tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);
        TextView tvHotelAddress = (TextView) view.findViewById(R.id.tvHotelAddress);
        TextView tvRoomPrice = (TextView) view.findViewById(R.id.tvRoomPrice);
        TextView tvArrival = (TextView) view.findViewById(R.id.tvArrival);
        TextView tvDeparture = (TextView) view.findViewById(R.id.tvDeparture);
        ButtonFlat btnState = (ButtonFlat) view.findViewById(R.id.btnState);
        ImageView image = (ImageView) view.findViewById(R.id.imageRoom);
        FrameLayout grayImage = (FrameLayout) view.findViewById(R.id.grayImage);
        BookedRoomSO historyRoom = history.get(position);

        tvHotelName.setText(historyRoom.getHotelName());
        tvHotelAddress.setText(historyRoom.getHotelAddress());
        tvRoomPrice.setText("$" + historyRoom.getRoomPrice());
        tvArrival.setText(historyRoom.getArrivalDate());
        tvDeparture.setText(historyRoom.getDepartureDate());

        String temp = "";
        if (historyRoom.getPhotoUrl() != null && !historyRoom.getPhotoUrl().equals("")) {
            temp = historyRoom.getPhotoUrl()
                    .substring(0, historyRoom.getPhotoUrl().length() - 5);
        }

        Glide.with(context)
                .load(temp + "z.jpg")
                .listener(listener)
                .into(image);

        switch (historyRoom.getOrderState()) {
            case BookedRoomSO.BOOKED:
                grayImage.setVisibility(View.GONE);
                btnState.setText("Booked");
                break;
            case BookedRoomSO.ACTIVE:
                grayImage.setVisibility(View.GONE);
                btnState.setText("Active");
                break;
            case BookedRoomSO.OUT_OF_DATE:
                grayImage.setVisibility(View.GONE);
                btnState.setText("Passed");
                break;
            case BookedRoomSO.CANCELLED:
                grayImage.setVisibility(View.VISIBLE);
                btnState.setText("Cancelled");
                break;
        }

        return view;
    }
}
