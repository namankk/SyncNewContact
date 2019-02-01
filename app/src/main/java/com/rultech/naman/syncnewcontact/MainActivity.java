package com.rultech.naman.syncnewcontact;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 444;
    private static final String TAG = "naman";
    private ListView mListView;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;
    ArrayList<ContactVD> contactVOList = new ArrayList<>();
    Cursor cursor;
    int counter;
    ContactVD contactVD;
    RecyclerView recyclerView;
    RecyclerView.Adapter madapter;
    RecyclerView.LayoutManager mlayoutManager;
    MyContentObserver myContentObserver;
    private MyAdapter mAdapter;
    String idss = "";
    int ispresent = 0;


    interface UpdateList {
        void onContactChange(String id);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

//        mAdapter = new MoviesAdapter(movieList);
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        });

        mAdapter = new MyAdapter(contactVOList, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        myContentObserver = new MyContentObserver(this, getContactCount(),listOfVersion(), new UpdateList() {
            @Override
            public void onContactChange(String id) {
                getContacts();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        /*getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myContentObserver);*/
        mAdapter.updateAdapter(contactVOList);
        Log.i(TAG, "onStart: called");
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType
                .CONNECTED).build();

        Data inputData = new Data.Builder()
                .putStringArray("myData",new String[1])
                .build();
        OneTimeWorkRequest uploadWork = new OneTimeWorkRequest.Builder(RegisterObserverWorker.class)
                .setConstraints(constraints).setInputData(inputData).build();

        //*WorkManager.getInstance().enqueue(locationWork);*//*
   /*     Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType
                .CONNECTED).addContentUriTrigger(ContactsContract.Contacts.CONTENT_URI,false).build();

        Data inputData = new Data.Builder()
                .putStringArray("myData",new String[1])
                .build();
        OneTimeWorkRequest uploadWork = new OneTimeWorkRequest.Builder(RegisterObserverWorker.class)
                .setConstraints(constraints).setInputData(inputData).build();
       *//* PeriodicWorkRequest locationWork = new PeriodicWorkRequest.Builder(MyBackgroundService
                .class,1,TimeUnit.MINUTES).addTag("naman")
                .setConstraints(constraints).build();*//*
        *//*WorkManager.getInstance().enqueue(locationWork);*//*
        WorkManager.getInstance().enqueue(uploadWork);*/
       /* Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType
                .CONNECTED).addContentUriTrigger(ContactsContract.Contacts.CONTENT_URI,false).build();

        Data inputData = new Data.Builder()
                .putStringArray("myData",new String[1])
                .build();
        OneTimeWorkRequest uploadWork = new OneTimeWorkRequest.Builder(RegisterObserverWorker.class)
                .setConstraints(constraints).setInputData(inputData).build();
       *//* PeriodicWorkRequest locationWork = new PeriodicWorkRequest.Builder(MyBackgroundService
                .class,1,TimeUnit.MINUTES).addTag("naman")
                .setConstraints(constraints).build();*//*
        *//*WorkManager.getInstance().enqueue(locationWork);*//*
        WorkManager.getInstance().enqueue(uploadWork);*/
    }

    private ArrayList<userModel> listOfVersion(){
        ArrayList<userModel> stringArrayList=new ArrayList<>();
        Cursor cursor = getBaseContext().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI, null, null, null,
                null);

        assert cursor != null;
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
            String version = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.VERSION));
            Log.i(TAG, "listOfVersion: id ="+id+" version = "+version);
            userModel userModel=new userModel();
            userModel.setUserId(id);
            userModel.setVersionCpde(version);
            stringArrayList.add(userModel);
        }
        cursor.close();
        return stringArrayList;
    }

    public void getContacts() {

        Cursor phones = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "(" + ContactsContract.Contacts._ID + ") DESC");


        while (phones.moveToNext()) {
            String phoneNumber="";
            String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String numberId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
            contactVD = new ContactVD();
            Cursor newphones = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, ContactsContract.RawContacts.CONTACT_ID + "=" + numberId, null, null);
            if (newphones.getCount() > 0) {
                newphones.moveToFirst();
                 phoneNumber = newphones.getString(newphones.getColumnIndex(ContactsContract.RawContacts.VERSION));
                contactVD.setContactNumber(phoneNumber);
            }
            newphones.close();



            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

          /*  if (idss.equals(numberId)) {
                ispresent++;
                Log.i(TAG, "getContacts: data is updated or inserted ");
            }*/
            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();

            contactVD.setId(numberId);
            contactVD.setContactName(name);

            contactVD.setContactImage(image_uri);
            contactVOList.add(contactVD);
            Log.i("TAG", "getContacts:" + name+" "+phoneNumber);
        }
        phones.close();
        pDialog.dismiss();
        if (ispresent <= 0) {
            Log.i(TAG, "getContacts: element deleted");
        }
    }

    @Override
    protected void onDestroy() {
       /* Intent intentService = new Intent(this, MyBackgroundService.class);
        stopService(intentService);*/
   /*     Log.i(TAG, "onDestroy: service stopped");
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType
                .CONNECTED).build();

        Data inputData = new Data.Builder()
                .putStringArray("myData",new String[1])
                .build();
        OneTimeWorkRequest uploadWork = new OneTimeWorkRequest.Builder(RegisterObserverWorker.class)
                .setConstraints(constraints).setInputData(inputData).build();
        WorkManager.getInstance().enqueue(uploadWork);*/
        Log.i(TAG, "onDestroy: workmanager start");
        super.onDestroy();

        /*getContentResolver().unregisterContentObserver(myContentObserver);*/



       /* Log.i(TAG, "onDestroy: called registered");
        Log.i(TAG, "onDestroy: called of service");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, OwnBroadcastReceiver.class);
        this.sendBroadcast(broadcastIntent);*/

    }
    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
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
}



