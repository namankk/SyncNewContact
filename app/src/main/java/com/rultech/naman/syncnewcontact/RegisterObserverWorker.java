package com.rultech.naman.syncnewcontact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RegisterObserverWorker extends Worker {
    Context mContext;
    WorkerParameters workerParams;
    String TAG = "naman";

    public RegisterObserverWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.workerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "doWork");
        Intent intentService = new Intent(mContext, MyBackgroundService.class);
        mContext.startService(intentService);
        return Result.success();
    }

}
