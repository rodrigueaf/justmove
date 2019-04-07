package com.claude.justmove.controller;

import com.claude.justmove.domain.Trip;
import com.claude.justmove.filterform.TripFilterForm;
import com.claude.justmove.service.MoveService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1")
public class MoveController {

    private MoveService moveService;

    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @PostMapping("trips/search")
    @ApiOperation(value = "Search fare for trips",
            response = Trip.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public List<Trip> search(@RequestBody TripFilterForm filterForm) {
        return moveService.searchTrips(filterForm);
    }
}
