package com.twinblade.smss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            DataHelper helper = new DataHelper(context);
            helper.open();
            for (SmsMessage message : messages) {
                try {
                    String body = new String(message.getUserData(), "UTF-8");

                    Log.e("SMSS", "NEW MSG: " + body + "; FROM: " + message.getOriginatingAddress());

                    helper.createMessage(message.getOriginatingAddress(), body, Message.STATUS_RECEIVED);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            helper.close();
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            broadcastManager.sendBroadcast(new Intent(Constants.INTENT_NEW_MESSAGE_RECEIVED));
        }
    }
}
