package com.rultech.naman.syncnewcontact;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 444;
    private static final String TAG = "kashyap";
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
        myContentObserver = new MyContentObserver(this,getContactCount(), new UpdateList() {
            @Override
            public void onContactChange(String id) {
                getContacts();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myContentObserver);

        mAdapter.updateAdapter(contactVOList);
    }

    public void getContacts() {

        Cursor phones = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, null);


        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String numberId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

          /*  if (idss.equals(numberId)) {
                ispresent++;
                Log.i(TAG, "getContacts: data is updated or inserted ");
            }*/
            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
            contactVD = new ContactVD();
            contactVD.setId(numberId);
            contactVD.setContactName(name);
            contactVD.setContactNumber(phoneNumber);
            contactVD.setContactImage(image_uri);
            contactVOList.add(contactVD);
            Log.i("TAG", "getContacts:" + name);
        }
        phones.close();
        pDialog.dismiss();
        if (ispresent <= 0) {
            Log.i(TAG, "getContacts: element deleted");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(myContentObserver);
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



