package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Services.GastronomyRevenueService;
import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GastronomyRevenueController {

    private final GastronomyRevenueService revenueService;

    public GastronomyRevenueController(GastronomyRevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/stats2")
    public ResponseEntity<List<GastronomyRevenue>> loadData() {
        try {
            List<GastronomyRevenue> data =
                    revenueService.loadGastronomyRevenueDataFromClasspath("data2.csv");
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // nie używany filtering, ale w razie co jest
    @GetMapping(value = "/stats2/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportStats2Csv(
            @RequestParam int minYear,
            @RequestParam int maxYear
    ) {
        try {
            byte[] csvBytes = revenueService.generateFilteredCsv("data2.csv", minYear, maxYear);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stats2_filtered.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}