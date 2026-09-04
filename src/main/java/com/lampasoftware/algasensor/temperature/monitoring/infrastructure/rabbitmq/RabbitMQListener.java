package com.lampasoftware.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import com.lampasoftware.algasensor.temperature.monitoring.api.model.TemperatureLogData;
import com.lampasoftware.algasensor.temperature.monitoring.domain.service.TemperatureMonitoringService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    // concurrency param means that spring initialize with at least 2 consumers and can increase up to three consumers.
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE_V_1_Q, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData,
                                         @Headers Map<String, Object> headers){
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info("Temperature updated: SensorId {} Temp {}", sensorId, temperature);
        log.info("Headers: {}", headers.toString());

        temperatureMonitoringService.processTemperatureReading(temperatureLogData);

        Thread.sleep(Duration.ofSeconds(5));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ALERT_TEMPERATURE_V_1_Q, concurrency = "2-3")
    @SneakyThrows
    public void handleAlertTemperature(@Payload TemperatureLogData temperatureLogData,
                                         @Headers Map<String, Object> headers){
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info("Temperature alert: SensorId {} Temp {}", sensorId, temperature);
        log.info("Headers: {}", headers.toString());

        Thread.sleep(Duration.ofSeconds(5));
    }
}
