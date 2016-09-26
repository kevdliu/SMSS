package com.twinblade.smss;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 0);
            } else {
                init();
            }
        }
    }

    private void init() {
        setContentView(R.layout.main);

        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText sendBox = (EditText) findViewById(R.id.send_box);
        final EditText recipientBox = (EditText) findViewById(R.id.recipient_box);

        final SmsManager smsManager = SmsManager.getDefault();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = sendBox.getText().toString();
                String recipient = recipientBox.getText().toString();

                if (!PhoneNumberUtils.isGlobalPhoneNumber(recipient)) {
                    Toast.makeText(Main.this, "Recipient phone number not valid", Toast.LENGTH_SHORT).show();
                    return;
                } else if (msg.equals("")) {
                    Toast.makeText(Main.this, "No message entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                smsManager.sendTextMessage(recipient, null, msg, null, null);
                Toast.makeText(Main.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           init();
        } else {
            Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
