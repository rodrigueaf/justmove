package com.claude.justmove.apiclient.ouibus;

import com.claude.justmove.domain.Trip;
import com.claude.justmove.domain.enumeration.CompanyName;
import com.claude.justmove.domain.enumeration.TransportationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OuibusApiClient {

    private RestTemplate restTemplate;
    private Environment env;

    public OuibusApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Trip> searchTrips(OuibusFilter ouibusFilter) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + env.getProperty("provider.ouibus.api-key"));
        headers.add("Content-Type", "application/json");
        headers.add("Content-Encoding", "gzip");
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    env.getProperty("provider.ouibus.base-url") + "search", HttpMethod.POST,
                    new HttpEntity<>(ouibusFilter, headers), Object.class);
            List<LinkedHashMap<String, Object>> tripsHashMap = (ArrayList) ((LinkedHashMap<String, Object>) response.getBody()).get("trips");
            return tripsHashMap.parallelStream()
                    .filter(l -> Boolean.parseBoolean(l.get("available").toString()))
                    .map(l -> {
                        Trip trip = new Trip();
                        trip.setDepartureTown(ouibusFilter.getOriginName());
                        trip.setArrivalTown(ouibusFilter.getDestinationName());
                        trip.setArrivalTime(LocalDateTime.parse(l.get("arrival").toString(), DateTimeFormatter.ISO_DATE_TIME)
                                .atZone(ZoneId.systemDefault()).toInstant());
                        trip.setDepartureTime(LocalDateTime.parse(l.get("departure").toString(), DateTimeFormatter.ISO_DATE_TIME)
                                .atZone(ZoneId.systemDefault()).toInstant());
                        trip.setCompanyName(CompanyName.OUIBUS);
                        trip.setPrice(Double.valueOf(l.get("price_cents").toString()) / 100);
                        trip.setTransportationType(TransportationType.BUS);
                        trip.setTripUrl(env.getProperty("provider.ouibus.base-url") + "search/" + l.get("id").toString() + "/book");
                        return trip;
                    }).collect(Collectors.toList());
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public List<OuibusStop> searchStops(String town, List<OuibusStop> ouibusStops) {
        return ouibusStops
                .parallelStream()
                .filter(s -> s.getShortName().toLowerCase().contains(town.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "ouibus_stops")
    public List<OuibusStop> findAllStops() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + env.getProperty("provider.ouibus.api-key"));
        headers.add("Content-Type", "application/json");
        headers.add("Content-Encoding", "gzip");
        ResponseEntity response = restTemplate.exchange(
                env.getProperty("provider.ouibus.base-url") + "stops", HttpMethod.GET,
                new HttpEntity<>(null, headers), Object.class);
        List<LinkedHashMap<String, Object>> stopsHashMap = (ArrayList) ((LinkedHashMap<String, Object>) response.getBody()).get("stops");
        return stopsHashMap.parallelStream()
                .map(l -> {
                    OuibusStop ouibusStop = new OuibusStop();
                    ouibusStop.setShortName(l.get("short_name").toString());
                    ouibusStop.setId(Integer.parseInt(l.get("id").toString()));
                    return ouibusStop;
                }).collect(Collectors.toList());
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
