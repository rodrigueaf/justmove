package com.claude.justmove.service;

import com.claude.justmove.apiclient.ouibus.OuibusApiClient;
import com.claude.justmove.apiclient.ouibus.OuibusFilter;
import com.claude.justmove.apiclient.ouibus.OuibusStop;
import com.claude.justmove.domain.Trip;
import com.claude.justmove.filterform.TripFilterForm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OuibusTripProvider implements TripProvider {

    private OuibusApiClient ouibusApiClient;

    public OuibusTripProvider(OuibusApiClient ouibusApiClient) {
        this.ouibusApiClient = ouibusApiClient;
    }

    @Override
    public List<Trip> searchTrips(TripFilterForm filterForm) {
        List<OuibusStop> allStops = ouibusApiClient.findAllStops();
        List<OuibusStop> departureStops = ouibusApiClient.searchStops(filterForm.getTrip().getDepartureTown(), allStops);
        List<OuibusStop> arrivalStops = ouibusApiClient.searchStops(filterForm.getTrip().getArrivalTown(), allStops);
        List<Trip> trips = new ArrayList<>();
        for (OuibusStop dStop : departureStops) {
            for (OuibusStop aStop : arrivalStops) {
                OuibusFilter ouibusFilter = new OuibusFilter(filterForm);
                ouibusFilter.setOriginId(dStop.getId());
                ouibusFilter.setDestinationId(aStop.getId());
                ouibusFilter.setOriginName(dStop.getShortName());
                ouibusFilter.setDestinationName(aStop.getShortName());
                trips.addAll(ouibusApiClient.searchTrips(ouibusFilter));
            }
        }
        return trips;
    }
}
