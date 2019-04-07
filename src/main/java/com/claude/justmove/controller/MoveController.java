package com.claude.justmove.controller;

import com.claude.justmove.domain.Trip;
import com.claude.justmove.filterform.TripFilterForm;
import com.claude.justmove.service.MoveService;
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

    @PostMapping("search")
    public List<Trip> search(@RequestBody TripFilterForm filterForm) {
        return moveService.searchTrips(filterForm);
    }
}
