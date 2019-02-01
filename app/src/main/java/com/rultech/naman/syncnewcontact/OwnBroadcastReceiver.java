package com.rultech.naman.syncnewcontact;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.impl.utils.ForceStopRunnable;

public class OwnBroadcastReceiver extends ForceStopRunnable.BroadcastReceiver {
    String TAG="naman";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ownBroadcastReceiver");
        context.startService(new Intent(context, MyBackgroundService.class));
    }
}
