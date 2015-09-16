 package com.todo.behtarinhotel.supportclasses;

 import android.app.DatePickerDialog;
 import android.app.DatePickerDialog.OnDateSetListener;
 import android.app.Dialog;
 import android.app.DialogFragment;
 import android.os.Build;
 import android.os.Bundle;

 import com.todo.behtarinhotel.R;

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
         DatePickerDialog dialog;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             dialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
         }else{
             dialog = new DatePickerDialog(getActivity(), R.style.Base_Theme_AppCompat_Dialog, ondateSet, year, month, day);
         }

         return dialog;
     }
 }