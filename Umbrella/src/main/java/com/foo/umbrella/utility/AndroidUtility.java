package com.foo.umbrella.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.foo.umbrella.R;
import com.foo.umbrella.ui.MainActivity;

/**
 * Created by gollaba on 9/18/17.
 */

public class AndroidUtility {




    public static void  showErrorDialog(final String errorTitle, final String message, Context context) {
            AlertDialog aDialog = new AlertDialog.Builder(context).setMessage(message).setTitle(errorTitle)
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,
                                            final int which) {


                        }
                    }).create();
            aDialog.show();
        }

    public static String getStringFromResource(Context context, int resId) {
        return context.getResources().getString(resId);
    }


    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(AndroidUtility.getStringFromResource(context, R.string.progressdialogmessage));
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        return progressDialog;
    }
}
