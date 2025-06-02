package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
import com.covid_stats.covid_stats.Services.GastronomyRevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GastronomyRevenueController {

    private final GastronomyRevenueService revenueService;

    public GastronomyRevenueController(GastronomyRevenueService revenueService) {
        this.revenueService = revenueService;
    }
    @GetMapping("/stats2")
    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    public ResponseEntity<List<GastronomyRevenue>> loadData() {
        try {
            Map<String, List<GastronomyRevenue>> dataMap =
                    revenueService.loadGastronomyRevenueDataFromClasspath("data2.csv");


            List<GastronomyRevenue> data = dataMap.values().stream().findFirst().orElse(List.of());

            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

}
