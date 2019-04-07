package com.claude.justmove.filterform;

import com.claude.justmove.domain.Trip;
import lombok.Data;

@Data
public class TripFilterForm {
    private Trip trip;
    private boolean sortByPriceAsc;
    private boolean sortByPriceDesc;
}
