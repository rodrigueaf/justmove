package com.claude.justmove.service;

import com.claude.justmove.domain.Trip;
import com.claude.justmove.filterform.TripFilterForm;

import java.util.List;

public interface TripProvider {

    List<Trip> searchTrips(TripFilterForm filterForm);
}
