package com.whiteboxteam.gliese.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.sync.application.ApplicationSyncService;
import com.whiteboxteam.gliese.ui.dialog.ConnectionErrorDialogFragment;
import com.whiteboxteam.gliese.ui.dialog.PositiveNegativeButtonListener;

public class StartAppActivity extends ActionBarActivity implements PositiveNegativeButtonListener {

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (isSyncCompleted(bundle)) {
                startMainActivity();
            } else {
                showErrorMessage();
            }
        }

        private boolean isSyncCompleted(Bundle bundle) {
            return bundle.getBoolean(ApplicationSyncService.SyncResultBroadcast.Parameters.RESULT);
        }
    };

    BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = new Message();
            msg.setData(intent.getExtras());
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        LocalBroadcastManager.getInstance(this).registerReceiver(syncCompleteReceiver, new IntentFilter
                (ApplicationSyncService.SyncResultBroadcast.ACTION));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(syncCompleteReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runApplicationSync();
    }

    private void runApplicationSync() {
        ApplicationSyncService.startApplicationSync(this);
    }

    private void showErrorMessage() {
        DialogFragment dialog = new ConnectionErrorDialogFragment();
        dialog.show(getSupportFragmentManager(), "connection-error");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        runApplicationSync();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        finish();
    }

    private void startMainActivity() {
        Intent intent = MainActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
