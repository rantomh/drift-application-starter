package com.rantomah.drift.api.service;

import com.rantomah.drift.api.dto.HealthDto;
import io.vertx.core.Future;

public interface HealthService {

    Future<HealthDto> getHealth();
}
