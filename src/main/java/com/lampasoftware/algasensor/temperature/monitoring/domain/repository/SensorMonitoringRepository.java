package com.lampasoftware.algasensor.temperature.monitoring.domain.repository;

import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
