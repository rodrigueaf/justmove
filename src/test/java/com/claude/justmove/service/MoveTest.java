package com.claude.justmove.service;

import com.claude.justmove.ApplicationTest;
import com.claude.justmove.controller.MoveController;
import com.claude.justmove.domain.Trip;
import com.claude.justmove.domain.enumeration.CompanyName;
import com.claude.justmove.domain.enumeration.TransportationType;
import com.claude.justmove.filterform.TripFilterForm;
import com.claude.justmove.util.TestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MoveTest extends ApplicationTest {

    private static final String DEFAULT_DEPARTURE_TOWN = "Paris";
    private static final String DEFAULT_ARRIVAL_TOWN = "Lille";
    private static final CompanyName DEFAULT_COMPANY_NAME = CompanyName.OUIBUS;
    private static final Double DEFAULT_PRICE = 10.0;
    private static final TransportationType DEFAULT_TRANSP_TYPE = TransportationType.BUS;
    private static final Instant DEFAULT_DEPARTURE_TIME = LocalDateTime
            .of(2019, 12, 30, 8, 30).atZone(ZoneId.systemDefault()).toInstant();
    private static final Instant DEFAULT_ARRIVAL_TIME = LocalDateTime
            .of(2019, 12, 30, 12, 30).atZone(ZoneId.systemDefault()).toInstant();

    private MockMvc mockMvc;
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    private TripProvider tripProvider;

    @Before
    public void setup() {
        tripProvider = mock(TripProvider.class);
        MoveService moveService = new MoveService();
        moveService.setOuibusTripProvider(tripProvider);
        MockitoAnnotations.initMocks(this);
        MoveController moveController = new MoveController(moveService);
        mockMvc = MockMvcBuilders.standaloneSetup(moveController)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    public static Trip getTrip() {
        Trip trip = new Trip();
        trip.setPrice(DEFAULT_PRICE);
        trip.setDepartureTown(DEFAULT_DEPARTURE_TOWN);
        trip.setArrivalTown(DEFAULT_ARRIVAL_TOWN);
        trip.setDepartureTime(DEFAULT_DEPARTURE_TIME);
        trip.setArrivalTime(DEFAULT_ARRIVAL_TIME);
        trip.setCompanyName(DEFAULT_COMPANY_NAME);
        trip.setTransportationType(DEFAULT_TRANSP_TYPE);
        return trip;
    }

    @Test
    public void shouldReturnTripsByCriteria() throws Exception {
        List<Trip> trips = new LinkedList<>();
        trips.add(getTrip());
        doReturn(trips)
                .when(tripProvider).searchTrips(isA(TripFilterForm.class));
        Trip trip = getTrip();
        TripFilterForm filterForm = new TripFilterForm();
        filterForm.setTrip(trip);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/search")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(filterForm))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].departureTown").value(CoreMatchers.hasItem(DEFAULT_DEPARTURE_TOWN)))
                .andExpect(jsonPath("$.[*].arrivalTown").value(CoreMatchers.hasItem(DEFAULT_ARRIVAL_TOWN)))
                .andExpect(jsonPath("$.[*].companyName").value(CoreMatchers.hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].transportationType").value(CoreMatchers.hasItem(DEFAULT_TRANSP_TYPE.toString())))
                .andExpect(jsonPath("$.[*].price").value(CoreMatchers.hasItem(DEFAULT_PRICE)));
    }
}
