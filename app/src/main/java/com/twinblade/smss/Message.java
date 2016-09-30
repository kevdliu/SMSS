package com.twinblade.smss;

public class Message {

    private long mId;
    private String mContact;
    private long mTime;
    private int mStatus;
    private String mBody;

    public Message(long id, String contact, long time, int status, String body) {
        mId = id;
        mContact = contact;
        mTime = time;
        mStatus = status;
        mBody = body;
    }

    public long getId() {
        return mId;
    }

    public String getContact() {
        return mContact;
    }

    public long getTime() {
        return mTime;
    }

    public int getStatus() {
        return mStatus;
    }

    public String getBody() {
        return mBody;
    }
}