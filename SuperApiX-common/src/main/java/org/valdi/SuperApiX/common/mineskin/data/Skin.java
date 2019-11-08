package org.valdi.SuperApiX.common.mineskin.data;

import com.google.gson.annotations.SerializedName;

public class Skin {
    private int id;
    private String name;
    private SkinData data;
    private long timestamp;
    @SerializedName("private")
    private boolean prvate;
    private int views;
    private int accountId;

    private double nextRequest;

    public Skin() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SkinData getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isPrivate() {
        return prvate;
    }

    public int getViews() {
        return views;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getNextRequest() {
        return nextRequest;
    }
}
