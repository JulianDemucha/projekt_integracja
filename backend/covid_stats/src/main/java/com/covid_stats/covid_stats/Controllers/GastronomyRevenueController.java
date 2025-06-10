package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
import com.covid_stats.covid_stats.Services.GastronomyRevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

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

    //nie uzywany filtering, ale w razie co jest
    @GetMapping(value = "/stats2/export", produces = "text/csv")
    public void exportStats2Csv(
            @RequestParam int minYear,
            @RequestParam int maxYear,
            HttpServletResponse response
    ) throws IOException {
        List<GastronomyRevenue> allData =
                revenueService.loadGastronomyRevenueDataFromClasspath("data2.csv");

        List<GastronomyRevenue> filtered = allData.stream()
                .filter(d -> d.getYear() >= minYear && d.getYear() <= maxYear)
                .collect(Collectors.toList());

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=stats2_filtered.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("year,revenue");
            for (GastronomyRevenue g : filtered) {
                writer.printf("%d,%.2f%n", g.getYear(), g.getRevenue());
            }
        }
    }
}
