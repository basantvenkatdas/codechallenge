package com.foo.umbrella.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.foo.umbrella.R;
import com.foo.umbrella.data.AppData;

/**
 * Created by gollaba on 9/17/17.
 */

public class ZipCodeSelector extends DialogFragment {


    private DialogFragmentDismissListener listener;

    public ZipCodeSelector() {
    }

    public static ZipCodeSelector newInstance(String title) {
        ZipCodeSelector frag = new ZipCodeSelector();
        return frag;
    }

    public void setDismissListener(DialogFragmentDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.zipcode_entry, null, false);
        final EditText editText = (EditText)view.findViewById(R.id.zipCodeEdittextView);
        editText.setText(AppData.instance(getActivity()).getZipCode());
        editText.setSelection(editText.getText().length());
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppData.instance(getActivity()).updateZipCode(editText.getText().toString());
                dismissDialog(dialog, true);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog(dialog, false);
            }

        });

        return alertDialogBuilder.create();
    }

    private void dismissDialog(DialogInterface dialog, boolean toUpdate) {
        if(dialog != null)
            dialog.dismiss();
        if(listener != null)
            listener.onDimiss(toUpdate);
    }
}
