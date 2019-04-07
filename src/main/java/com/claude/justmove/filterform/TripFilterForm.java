package com.claude.justmove.filterform;

import com.claude.justmove.domain.Trip;

public class TripFilterForm {
    private Trip trip;
    private boolean sortByPriceAsc;
    private boolean sortByPriceDesc;

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public boolean isSortByPriceAsc() {
        return sortByPriceAsc;
    }

    public void setSortByPriceAsc(boolean sortByPriceAsc) {
        this.sortByPriceAsc = sortByPriceAsc;
    }

    public boolean isSortByPriceDesc() {
        return sortByPriceDesc;
    }

    public void setSortByPriceDesc(boolean sortByPriceDesc) {
        this.sortByPriceDesc = sortByPriceDesc;
    }
}
