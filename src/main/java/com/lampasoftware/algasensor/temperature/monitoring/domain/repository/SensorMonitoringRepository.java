package com.lampasoftware.algasensor.temperature.monitoring.domain.repository;

import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.math.BigInteger;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
