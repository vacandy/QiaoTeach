package com.qiao_techcomponent.qiao_eventbus;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable{

    //请求的对象  RequestBean 对应的json字符串
    private String mDataStr;


    //    请求对象的类型
    private int mType;

    protected Request(Parcel in) {
        mDataStr = in.readString();
        mType = in.readInt();
    }

    public Request(String data, int type) {
        mDataStr = data;
        mType = type;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDataStr);
        dest.writeInt(mType);
    }
}
