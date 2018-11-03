/**
 *
 *  Author: P. Torres
 *  Last Modified: 10/26/18
 */

package com.torrestudio.countitdown.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.torrestudio.countitdown.R;

public class SortByDialogFragment extends DialogFragment {

    public interface OnSortOptionSelected {
        void onSortOptionSelected(int sortOption);
    }

    private OnSortOptionSelected mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSortOptionSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSortOptionSelected");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_sort_by)
                .setItems(R.array.sort_by_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        mCallback.onSortOptionSelected(which);
                    }
                });

        return builder.create();
    }
}
