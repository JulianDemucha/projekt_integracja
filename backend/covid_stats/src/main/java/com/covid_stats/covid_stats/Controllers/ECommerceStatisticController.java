package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Services.ECommerceStatisticService;
import com.covid_stats.covid_stats.DTO.ECommerceEnterprisesPercent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ECommerceStatisticController {
    private final ECommerceStatisticService service;

    public ECommerceStatisticController(ECommerceStatisticService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping("/api/stats")
    public List<ECommerceEnterprisesPercent> stats(
            @RequestParam(name="from", defaultValue="2013") int from,
            @RequestParam(name="to",   defaultValue="2023") int to) {
        return service.getStats(from, to);
    }
}
