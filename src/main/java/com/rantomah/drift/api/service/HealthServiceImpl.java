package com.rantomah.drift.api.service;

import com.rantomah.drift.api.dto.HealthDto;
import com.rantomah.drift.api.event.TestEvent;
import com.rantomah.drift.api.mapper.HealthMapper;
import com.rantomah.drift.api.model.Health;
import com.rantomah.drift.framework.annotation.di.Inject;
import com.rantomah.drift.framework.annotation.stereotype.Service;
import com.rantomah.drift.framework.core.Environment;
import com.rantomah.drift.framework.core.event.EventPublisher;

@Service
public class HealthServiceImpl implements HealthService {

    @Inject private Environment environment;

    @Inject private EventPublisher eventPublisher;

    @Override
    public HealthDto getHealth() {
        Health health = new Health();
        health.setStatus("UP");
        health.setVersion(environment.getProperty("app.version"));

        eventPublisher.publish(new TestEvent("admin", "admin@drift.io"));

        return HealthMapper.INSTANCE.toDto(health);
    }
}
