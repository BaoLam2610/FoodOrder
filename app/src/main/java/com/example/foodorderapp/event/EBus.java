package com.example.foodorderapp.event;

import org.greenrobot.eventbus.EventBus;

public class EBus {
    static EBus instance;

    public static EBus getInstance() {
        if (instance == null) instance = new EBus();
        return instance;
    }

    public void post(Object o) {
        EventBus.getDefault().post(o);
    }

    public void register(Object o) {
        if (!EventBus.getDefault().isRegistered(o)) EventBus.getDefault().register(o);
    }

    public void unRegister(Object o){
        EventBus.getDefault().unregister(o);
    }
}
