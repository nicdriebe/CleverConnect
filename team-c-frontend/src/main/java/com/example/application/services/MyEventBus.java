package com.example.application.services;

import com.google.common.eventbus.EventBus;

public class MyEventBus {
    private static final EventBus INSTANCE = new EventBus();

    private MyEventBus() {
        // Private constructor to prevent instantiation
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }
}