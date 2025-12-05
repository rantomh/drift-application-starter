package com.rantomah.drift.api;

import com.rantomah.drift.framework.annotation.DriftApplication;
import com.rantomah.drift.framework.core.DriftRunner;

@DriftApplication
public class Application {

    public static void main(String[] args) {
        DriftRunner.run(Application.class);
    }
}
