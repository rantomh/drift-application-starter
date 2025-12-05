package com.rantomah.drift.framework.core;

import org.slf4j.LoggerFactory;

public final class Logger {

    private Logger() {}

    public static final org.slf4j.Logger access = LoggerFactory.getLogger("com.rantomah.drift.access");
    public static final org.slf4j.Logger log = LoggerFactory.getLogger("com.rantomah.drift.log");
    public static final org.slf4j.Logger exception = LoggerFactory.getLogger("com.rantomah.drift.exception");

}
