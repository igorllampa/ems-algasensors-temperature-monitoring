package com.lampasoftware.algasensor.temperature.monitoring.domain.repository;

import com.lampasoftware.algasensor.temperature.monitoring.domain.model.TemperatureLog;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.TemperatureLogId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {
}
