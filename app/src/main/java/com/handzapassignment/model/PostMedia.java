package com.handzapassignment.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.handzapassignment.utils.MediaUtils;

public class PostMedia implements Parcelable{

    public String mediaPath;
    public MediaUtils.MediaType mediaType;

    public PostMedia(){}

    protected PostMedia(Parcel in) {
        mediaPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostMedia> CREATOR = new Creator<PostMedia>() {
        @Override
        public PostMedia createFromParcel(Parcel in) {
            return new PostMedia(in);
        }

        @Override
        public PostMedia[] newArray(int size) {
            return new PostMedia[size];
        }
    };
}
