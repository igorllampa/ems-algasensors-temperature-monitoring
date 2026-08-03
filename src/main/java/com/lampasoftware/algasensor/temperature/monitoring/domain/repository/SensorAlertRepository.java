package com.lampasoftware.algasensor.temperature.monitoring.domain.repository;

import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorAlert;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
