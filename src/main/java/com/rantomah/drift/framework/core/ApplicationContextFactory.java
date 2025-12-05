package com.rantomah.drift.framework.core;

import com.rantomah.drift.framework.core.impl.ApplicationContextImpl;

public class ApplicationContextFactory {

    private ApplicationContextFactory() {}

    public static ApplicationContext create() {
        return new ApplicationContextImpl();
    }
}
