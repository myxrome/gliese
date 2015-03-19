package com.whiteboxteam.gliese.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.entity.FactEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
import com.whiteboxteam.gliese.data.helper.statistic.SessionHelper;
import com.whiteboxteam.gliese.data.sync.application.ApplicationSyncService;
import com.whiteboxteam.gliese.ui.dialog.ConnectionErrorDialogFragment;
import com.whiteboxteam.gliese.ui.dialog.PositiveNegativeButtonListener;

public class StartAppActivity extends ActionBarActivity implements PositiveNegativeButtonListener {

    private static final String APPLICATION_START_COUNTER_EVENT = "APPLICATION_START_COUNTER";
    private static final String UPLOAD_SCREEN_TIMER_EVENT = "UPLOAD_SCREEN_TIMER";
    private static final String ORIENTATION_TIMER_EVENT = "ORIENTATION_TIMER";
    private static final int APPLICATION_ID = 1;
    private static final int UPLOAD_SCREEN_ID = 1;
    private static final int LANDSCAPE_ORIENTATION_ID = 1;
    private static final int PORTRAIT_ORIENTATION_ID = 2;
    private FactHelper factHelper;
    private FactEntity uploadScreenTimer;
    private FactEntity orientationTimer;

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

        factHelper = FactHelper.getInstance(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(syncCompleteReceiver, new IntentFilter
                (ApplicationSyncService.SyncResultBroadcast.ACTION));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(syncCompleteReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        factHelper.finishTimer(orientationTimer);
        factHelper.finishTimer(uploadScreenTimer);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionHelper.getInstance(this).start();
        factHelper.increaseCounter(APPLICATION_ID, FactHelper.APPLICATION_CONTEXT, APPLICATION_START_COUNTER_EVENT);
        uploadScreenTimer = factHelper.startTimer(UPLOAD_SCREEN_ID, FactHelper.SCREEN_CONTEXT,
                UPLOAD_SCREEN_TIMER_EVENT);
        int orientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                LANDSCAPE_ORIENTATION_ID : PORTRAIT_ORIENTATION_ID;
        orientationTimer = factHelper.startTimer(orientation, FactHelper.ORIENTATION_CONTEXT, ORIENTATION_TIMER_EVENT);
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
