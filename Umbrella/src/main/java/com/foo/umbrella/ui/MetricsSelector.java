package com.foo.umbrella.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.foo.umbrella.R;
import com.foo.umbrella.data.AppData;

/**
 * Created by gollaba on 9/17/17.
 */

public class MetricsSelector extends DialogFragment {

    private CharSequence[] items = new CharSequence[]{"Centigrade", "Farenheit"};
    private int mSelectedIndex = 0;
    private int currentSelectedItem = 0;
    private DialogFragmentDismissListener listener;

    public MetricsSelector() {
    }

    public static MetricsSelector newInstance(String title) {
        MetricsSelector frag = new MetricsSelector();
        return frag;
    }

    public void setDismissListener(DialogFragmentDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Select the metric System");
        //alertDialogBuilder.setView(view);
        currentSelectedItem =  AppData.instance(getActivity()).getMetricsSetting();
        alertDialogBuilder.setPositiveButton("OK",  null);
        alertDialogBuilder.setNegativeButton("Cancel", null);
        alertDialogBuilder.setSingleChoiceItems(items, currentSelectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        mSelectedIndex = which;
            }
        });
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mSelectedIndex >= 0 && mSelectedIndex != currentSelectedItem) {
                    AppData.instance(getActivity()).updateMetricsSetting(mSelectedIndex);
                    dismissDialog(dialog, true);
                }else{
                    dismissDialog(dialog, false);
                }
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

