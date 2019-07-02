package com.qiao_techcomponent.qiao_eventbus;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable{

    protected Response(Parcel in) {
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
