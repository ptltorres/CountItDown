package com.torrestudio.countitdown.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment
                                implements DatePickerDialog.OnDateSetListener {

    // Callback interface to notify other classes when the user selects a date
    public interface OnDateSelectedListener {
        void onDateSelected(DatePicker view, int year, int month, int dayOfMonth);
    }

    private OnDateSelectedListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnDateSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDateSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mCallback != null) {
            mCallback.onDateSelected(view, year, month, dayOfMonth);
        }
    }
}
