package com.example.zhangh.appsize.util;

import android.os.Bundle;

public class EventBusBundle {

    private int code;
    private Bundle bundle;

    public EventBusBundle(int code) {
        this.code = code;
    }

    public EventBusBundle(int code, Bundle bundle) {
        this.code = code;
        this.bundle = bundle;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
