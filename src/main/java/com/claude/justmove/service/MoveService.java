package com.claude.justmove.service;

import com.claude.justmove.domain.Trip;
import com.claude.justmove.filterform.TripFilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MoveService {

    private TripProvider ouibusTripProvider;

    public List<Trip> searchTrips(TripFilterForm filterForm) {
        // Pre search
        // chain other trips provider results
        List<Trip> trips = ouibusTripProvider.searchTrips(filterForm);
        trips = postSearch(filterForm, trips);
        return trips;
    }

    private List<Trip> postSearch(TripFilterForm filterForm, List<Trip> trips) {
        return trips.stream()
                .peek(t -> {
                    t.setId(UUID.randomUUID().toString());
                    t.setDuration(Duration.between(t.getDepartureTime(), t.getArrivalTime()).getSeconds() / 60);
                }).sorted((o1, o2) -> {
                    if (filterForm.isSortByPriceAsc())
                        return o1.getPrice().compareTo(o2.getPrice());
                    else if (filterForm.isSortByPriceDesc())
                        return o2.getPrice().compareTo(o1.getPrice());
                    else
                        return o1.getDepartureTown().compareTo(o2.getDepartureTown());
                }).collect(Collectors.toList());
    }

    @Autowired
    @Qualifier("ouibusTripProvider")
    public void setOuibusTripProvider(TripProvider ouibusTripProvider) {
        this.ouibusTripProvider = ouibusTripProvider;
    }
}
