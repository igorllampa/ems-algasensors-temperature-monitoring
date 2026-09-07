package com.lampasoftware.algasensor.temperature.monitoring.domain.service;

import com.lampasoftware.algasensor.temperature.monitoring.api.model.SensorAlertOutput;
import com.lampasoftware.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {

                    if (alert.getMaxTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {

                        log.info("Alert temperature MAX: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());

                    } else if (alert.getMinTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {

                        log.info("Alert temperature MIN: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    } else {
                        logIgnoredTemperature(temperatureLogData);
                    }

                }, () -> {
                    logIgnoredTemperature(temperatureLogData);
                });
    }

    private static void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Alert ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
