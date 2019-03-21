package com.example.zhangh.appsize.util;

public class EventBusCode {

    private volatile static EventBusCode instance = null;

    public static EventBusCode getInstance() {
        if (instance == null) {
            synchronized (EventBusCode.class) {
                if (instance == null) {
                    instance = new EventBusCode();
                }
            }
        }

        return instance;
    }

    public int RESTART = 1001;
}
