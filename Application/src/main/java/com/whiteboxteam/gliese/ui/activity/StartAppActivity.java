package com.whiteboxteam.gliese.ui.activity;

import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import com.whiteboxteam.gliese.R;
import com.whiteboxteam.gliese.data.entity.FactEntity;
import com.whiteboxteam.gliese.data.helper.statistic.FactHelper;
import com.whiteboxteam.gliese.data.helper.statistic.SessionHelper;
import com.whiteboxteam.gliese.data.sync.application.ApplicationSyncService;
import com.whiteboxteam.gliese.ui.custom.materialdialogs.MaterialDialog;

public class StartAppActivity extends ActionBarActivity {

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
        if (getResources().getBoolean(R.bool.force_portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        setContentView(R.layout.activity_start_app);

        factHelper = FactHelper.getInstance(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(syncCompleteReceiver, new IntentFilter(
                ApplicationSyncService.SyncResultBroadcast.ACTION));
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
        factHelper.increaseCounter(FactHelper.VirtualContext.APPLICATION_ID, FactHelper.ContextType.VIRTUAL_CONTEXT,
                                   FactHelper.EventTag.APPLICATION_START_COUNTER_EVENT);
        uploadScreenTimer = factHelper
                .startTimer(FactHelper.VirtualContext.UPLOAD_SCREEN_ID, FactHelper.ContextType.VIRTUAL_CONTEXT,
                            FactHelper.EventTag.UPLOAD_SCREEN_TIMER_EVENT);
        int orientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                FactHelper.VirtualContext.LANDSCAPE_ORIENTATION_ID : FactHelper.VirtualContext.PORTRAIT_ORIENTATION_ID;
        orientationTimer = factHelper.startTimer(orientation, FactHelper.ContextType.VIRTUAL_CONTEXT,
                                                 FactHelper.EventTag.ORIENTATION_TIMER_EVENT);
        runApplicationSync();
    }

    private void runApplicationSync() {
        ApplicationSyncService.startApplicationSync(this);
    }

    private void showErrorMessage() {
        new MaterialDialog.Builder(this).title(R.string.application_error_error_title)
                                        .content(R.string.application_error_error_message)
                                        .positiveText(R.string.retry_button_title)
                                        .negativeText(R.string.exit_button_title)
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog dialog) {
                                                runApplicationSync();
                                            }

                                            @Override
                                            public void onNegative(MaterialDialog dialog) {
                                                SessionHelper.getInstance(StartAppActivity.this).finish();
                                                finish();
                                            }
                                        }).cancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SessionHelper.getInstance(StartAppActivity.this).finish();
                finish();
            }
        }).show();
    }

    private void startMainActivity() {
        Intent intent = MainActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
