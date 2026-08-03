package com.lampasoftware.algasensor.temperature.monitoring.api.controller;


import com.lampasoftware.algasensor.temperature.monitoring.api.model.SensorAlertInput;
import com.lampasoftware.algasensor.temperature.monitoring.api.model.SensorAlertOutput;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorAlert;
import com.lampasoftware.algasensor.temperature.monitoring.domain.model.SensorId;
import com.lampasoftware.algasensor.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping("/{sensorId}/alert")
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput getSensorAlert(@PathVariable TSID sensorId){
        SensorAlert sensorAlert = findById(sensorId);
        return convertOutputModel(sensorAlert);
    }

    @PutMapping("/{sensorId}/alert")
    @ResponseStatus(HttpStatus.OK)
    public void createOrUpdate(@PathVariable TSID sensorId, @RequestBody SensorAlertInput sensorAlertInput){

        SensorAlert sensorAlert = findByIdOrDefault(sensorId);

        sensorAlert.setMaxTemperature(sensorAlertInput.getMaxTemperature());
        sensorAlert.setMinTemperature(sensorAlertInput.getMinTemperature());
        sensorAlertRepository.saveAndFlush(sensorAlert);
    }

    @DeleteMapping("/{sensorId}/alert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensorAlert(@PathVariable TSID sensorId){
        SensorAlert sensorAlert = findById(sensorId);

        sensorAlertRepository.delete(sensorAlert);
    }


    private SensorAlert findById(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private SensorAlert findByIdOrDefault(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .minTemperature(null)
                        .maxTemperature(null)
                        .build());
    }

    private static SensorAlertOutput convertOutputModel(SensorAlert sensorAlert) {
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .minTemperature(sensorAlert.getMinTemperature())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .build();
    }
}
