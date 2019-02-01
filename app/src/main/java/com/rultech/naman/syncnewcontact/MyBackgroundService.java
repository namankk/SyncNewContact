package com.rultech.naman.syncnewcontact;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyBackgroundService extends Service {
    String TAG = "naman";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyContentObserver myContentObserver = new MyContentObserver(getBaseContext(), getContactCount(),listOfVersion(), new MainActivity.UpdateList() {
            @Override
            public void onContactChange(String id) {

            }
        });

        getBaseContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myContentObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called ");
        MyContentObserver myContentObserver = new MyContentObserver(getBaseContext(), getContactCount(),listOfVersion(), new MainActivity.UpdateList() {
            @Override
            public void onContactChange(String id) {

            }
        });

        getBaseContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myContentObserver);
        return START_STICKY;
    }

    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = getBaseContext().getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    null);
            if (cursor != null) {
                return cursor.getCount();
            } else {
                return 0;
            }
        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
    private ArrayList<userModel> listOfVersion(){
        ArrayList<userModel> stringArrayList=new ArrayList<>();
        Cursor cursor = getBaseContext().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI, null, null, null,
                null);
        userModel userModel=new userModel();
        assert cursor != null;
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
            String version = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.VERSION));
            userModel.setUserId(id);
            userModel.setVersionCpde(version);
            stringArrayList.add(userModel);
        }
        cursor.close();
        return stringArrayList;
    }

    @Override
    public void onDestroy() {
stopSelf();
        Log.i(TAG, "onDestroy: stoped");
        super.onDestroy();

    }
/*    @Override
    public void onTaskRemoved(Intent rootIntent){
     *//*   try {
            Intent intent = new Intent("com.android.techtrainner");
            intent.putExtra("yourvalue", "torestore");
            sendBroadcast(intent);
            Log.i(TAG, "onDestroy: called of event code executed");
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(this.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);
        super.onTaskRemoved(rootIntent);
    }*/
}
