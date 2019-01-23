package com.rultech.naman.syncnewcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MyContentObserver extends ContentObserver {
    String TAG = "naman";
    MainActivity.UpdateList myHandler;

    Context myContext;
    ArrayList<ContactVD> newDataBase = new ArrayList<>();
    ContactVD contactVD;
    ArrayList<ContactVD> lastUpdatedContact = new ArrayList<>();
    ArrayList<ContactVD> oldDataBase;
    private int mContactCount;
    String idDelete;

    public MyContentObserver(Context context, int cursorCount, MainActivity.UpdateList updateList) {
        super(null);
        myHandler = updateList;
        myContext = context;
        mContactCount = cursorCount;
    }

    String id;
    String lastChangedContact;
    private static final String ACCOUNT_TYPE = "com.android.account.youraccounttype";
   /* private static final String WHERE_MODIFIED = "( " +
            ContactsContract.RawContacts.DIRTY + "=1 )";*/


    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.i(TAG, "onChange: called");
        final int currentCount = getContactCount();
        if (currentCount < mContactCount) {
            // DELETE HAPPEN.
            Log.w(TAG, "deleted");
            afterDeleteDataBase();
        } else if (currentCount == mContactCount) {
            // UPDATE HAPPEN.
            if (!selfChange) {
                Log.w(TAG, "updated");
                getUpdatedOrInsertedContact();

            }

        } else {
            // INSERT HAPPEN.
            if (!selfChange) {
                Log.w(TAG, "inserted");
                getNewInsertedList(selfChange);
            }


        }
        mContactCount = currentCount;

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.i(TAG, "uri change occur: " + uri);
    }

    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = myContext.getContentResolver().query(
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

    private void getUpdatedOrInsertedContact() {

        try {

            Cursor cursor = myContext.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " Desc");
            if (cursor.moveToFirst()) {
                id = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

                String name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.w("Contact ID", id);
                Log.w("Person Name", name);

            }
            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNewInsertedList(boolean selfChange) {
        if (!selfChange) {
            try {
                if (ActivityCompat.checkSelfPermission(myContext,
                        Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver cr = myContext.getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        //moving cursor to last position
                        //to get last element added
                        cursor.moveToLast();
                        String contactName = null, photo = null, contactNumber = null;
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    if (contactNumber != null && contactNumber.length() > 0) {
                                        contactNumber = contactNumber.replace(" ", "");
                                    }
                                    contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    String ids = pCur.getString(pCur.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                                    String msg = "Name : " + contactName + " Contact No. : " + ids;
                                    //Displaying result
                                    Log.w("Contact ID", msg);

                                }
                                pCur.close();
                            }
                        }
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void afterDeleteDataBase() {

        Cursor c = myContext.getContentResolver().query(ContactsContract.DeletedContacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        assert c != null;
        if (c.getCount() > 0) {

            c.moveToLast();
            String _ID = c.getString(c.getColumnIndex(ContactsContract.DeletedContacts.CONTACT_ID));

            Log.w("colunm", _ID);


            c.close();

        }

    }

}



