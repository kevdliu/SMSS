package com.twinblade.smss;

public class Key {

    private long mId;
    private String mContact;
    private String mPublic;
    private String mPrivate;

    public Key(long id, String contact, String publicKey, String privateKey) {
        mId = id;
        mContact = contact;
        mPublic = publicKey;
        mPrivate = privateKey;
    }

    public long getId() {
        return mId;
    }

    public String getContact() {
        return mContact;
    }

    public String getPublic() {
        return mPublic;
    }

    public String getPrivate() {
        return mPrivate;
    }
}
