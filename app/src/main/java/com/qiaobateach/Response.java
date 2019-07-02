package com.qiaobateach;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable{

    //响应对象
    private String mDataStr;

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
