 package com.todo.behtarinhotel.supportclasses;

 import android.app.DatePickerDialog;
 import android.app.DatePickerDialog.OnDateSetListener;
 import android.app.Dialog;
 import android.os.Bundle;
 import android.app.DialogFragment;

 import java.util.Calendar;

 public class DatePickerFragment extends DialogFragment {
     OnDateSetListener ondateSet;

     public DatePickerFragment() {
     }

     public void setCallBack(OnDateSetListener ondate) {
         ondateSet = ondate;
     }

     private int year, month, day;

     @Override
     public void setArguments(Bundle args) {
         super.setArguments(args);
         year = args.getInt("year");
         month = args.getInt("month");
         day = args.getInt("day");
     }

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         DatePickerDialog dialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
         dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

         return dialog;
     }
 }