package com.claude.justmove.domain;

import com.claude.justmove.domain.enumeration.CompanyName;
import com.claude.justmove.domain.enumeration.TransportationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString
@NoArgsConstructor
public class Trip {

    private String id;
    private Double price;
    private String departureTown;
    private String arrivalTown;
    private Instant departureTime;
    private Instant arrivalTime;
    private CompanyName companyName;
    private TransportationType transportationType;
    private String tripUrl;
    private long duration;
}
