package com.rantomah.drift.api.event;

import com.rantomah.drift.framework.annotation.EventHandler;
import com.rantomah.drift.framework.annotation.EventListener;
import com.rantomah.drift.framework.core.Logger;

@EventHandler
public class TestEventHandler {

    @EventListener
    public void onTestEvent(TestEvent event) {
        Logger.log.info("TestEvent consumed with success! {}", event.getEmail());
    }
}
