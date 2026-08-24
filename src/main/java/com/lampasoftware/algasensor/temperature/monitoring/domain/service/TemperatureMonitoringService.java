package com.lampasoftware.algasensor.temperature.monitoring.domain.service;

import com.lampasoftware.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorMonitoring;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.TemperatureLog;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.TemperatureLogId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.lampasoftware.algasensor.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData){

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData));
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .build();

            temperatureLogRepository.save(temperatureLog);
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature Ignored: SensorId {} Temperature {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
