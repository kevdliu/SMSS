package com.twinblade.smss;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_CONTACTS},
                        0);
            } else {
                init();
            }
        }
    }

    private void init() {
        setContentView(R.layout.main);

        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText sendBox = (EditText) findViewById(R.id.send_box);
        final AutoCompleteTextView recipientBox = (AutoCompleteTextView) findViewById(R.id.recipient_box);

        final HashMap<String, String> contactsMap = getContactsMap();
        if (contactsMap.size() > 0) {
            final String[] contactsNameArray = new String[contactsMap.size()];
            contactsMap.keySet().toArray(contactsNameArray);

            ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, contactsNameArray);
            recipientBox.setAdapter(contactsAdapter);
            recipientBox.setThreshold(2);
            recipientBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String name = (String) adapterView.getItemAtPosition(position);
                    String phoneNumber = contactsMap.get(name);

                    //TODO: Check if phone number is paired

                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }
            });
        }

        final SmsManager smsManager = SmsManager.getDefault();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = sendBox.getText().toString();
                String recipient = recipientBox.getText().toString().trim();
                String recipientPhoneNumber;

                if (contactsMap.containsKey(recipient)) {
                    recipientPhoneNumber = contactsMap.get(recipient);
                } else if (PhoneNumberUtils.isGlobalPhoneNumber(recipient)) {
                    recipientPhoneNumber = recipient;
                } else {
                    Toast.makeText(Main.this, "Recipient not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msg.equals("")) {
                    Toast.makeText(Main.this, "No message entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    smsManager.sendDataMessage(recipientPhoneNumber, null, Constants.DATA_SMS_PORT, msg.getBytes("UTF-8"), null, null);
                    Toast.makeText(Main.this, "Sending...", Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private HashMap<String, String> getContactsMap() {
        HashMap<String, String> contactsMap = new HashMap<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor contactCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (contactCursor == null) {
            return contactsMap;
        }

        while (contactCursor.moveToNext()) {
            String id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if (contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                Cursor phoneNumberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);
                if (phoneNumberCursor == null) {
                    continue;
                }

                int phoneNumberCount = 1;
                while (phoneNumberCursor.moveToNext()) {
                    String phoneNumber = phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));

                    String countSuffix = "";
                    if (phoneNumberCount > 1) {
                        countSuffix = " " + Integer.toString(phoneNumberCount);
                    }

                    contactsMap.put(name + countSuffix, phoneNumber);
                    phoneNumberCount++;
                }
                phoneNumberCursor.close();
            }
        }
        contactCursor.close();

        return contactsMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           init();
        } else {
            Toast.makeText(this, "SMS permissions denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
