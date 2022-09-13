package com.github.youssefagagg.automaticirrigationsystem.config.dbmigrations;

import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.Sensor;
import com.github.youssefagagg.automaticirrigationsystem.domain.Slot;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.CropType;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * MongoDB database migrations using Mongock.
 * Creates the initial database setup.
 */
@ChangeUnit(id = "initialization", order = "1")
@RequiredArgsConstructor
public class InitialSetupMigration {

    private final MongoTemplate template;
    private static final Random random = new Random();

    @Execution
    public void changeSet() {
        var s1 = creatSensor("sensor-1", "s-1");
        var s2 = creatSensor("sensor-2", "s-2");
        var s3 =  creatSensor("sensor-3", "s-3");
        template.save(s1);
        template.save(s2);
        template.save(s3);

        var p1 = creatPlot("plot-1","p-1", s1);
        var p2 = creatPlot("plot-2","p-2", s2);
        var p3 = creatPlot("plot-3","p-3", s3);
        p1.setSlots(creatSlots(p1));
        template.save(p1);
        p2.setSlots(creatSlots(p2));
        template.save(p2);
        p3.setSlots(creatSlots(p3));
        template.save(p3);
    }

    private List<Slot> creatSlots(Plot plot) {
        return IntStream
                .rangeClosed(0,random.nextInt(2,10))
                .mapToObj(i -> {
                    var slot =new Slot();
                    slot.setStatus(Status.DOWN);
                    template.save(slot);
                    return slot;
                }).collect(Collectors.toList());
    }

    private Sensor creatSensor(String id,String code) {
        var sensor= new Sensor();
        sensor.setId(id);
        sensor.setSensorCode(code);
        sensor.setStatus(Status.DOWN);
        return sensor;
    }

    private Plot creatPlot(String id, String code, Sensor sensor){
        var plot = new Plot();
        plot.setId(id);
        plot.setPlotCode(code);
        plot.setPlotLength(random.nextDouble(20,1000));
        plot.setPlotWidth(random.nextDouble(20,1000));
        plot.setCropType(CropType.RICE);
        plot.setSensor(sensor);
        plot.setWaterAmount(random.nextInt(20,1000));
        plot.setHasAlert(false);
        plot.setLastIrrigationTime(Instant.EPOCH.plusSeconds(60*60*24*random.nextInt(1,50)));
        plot.setIsIrrigated(false);

        return plot;

    }

}
