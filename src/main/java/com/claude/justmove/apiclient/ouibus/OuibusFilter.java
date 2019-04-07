package com.claude.justmove.apiclient.ouibus;

import com.claude.justmove.filterform.TripFilterForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class OuibusFilter {

    @JsonProperty(value = "origin_id")
    private int originId;
    @JsonProperty(value = "destination_id")
    private int destinationId;
    private String date;
    private List<Passenger> passengers;
    @JsonIgnore
    private String originName;
    @JsonIgnore
    private String destinationName;

    public OuibusFilter(TripFilterForm filterForm) {
        this.date = filterForm.getTrip().getDepartureTime().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        this.passengers = Collections.singletonList(new Passenger(1, 30));
    }

    public static class Passenger {
        public int id;
        public int age;

        public Passenger(int id, int age) {
            this.id = id;
            this.age = age;
        }
    }
}
