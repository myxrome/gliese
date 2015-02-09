package com.whiteboxteam.gliese.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.whiteboxteam.gliese.R;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 05.06.2014
 * Time: 15:35
 */

public class ConnectionErrorDialogFragment extends DialogFragment {

    private PositiveNegativeButtonListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PositiveNegativeButtonListener) activity;
        } catch (Exception e) {

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.application_error_error_title)).
                setMessage(getResources().getString(R.string.application_error_error_message)).
                setPositiveButton(getResources().getString(R.string.retry_button_title), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(ConnectionErrorDialogFragment.this);
                    }
                }).
                setNegativeButton(getResources().getString(R.string.exit_button_title), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(ConnectionErrorDialogFragment.this);
                    }
                });
        return builder.create();
    }
}