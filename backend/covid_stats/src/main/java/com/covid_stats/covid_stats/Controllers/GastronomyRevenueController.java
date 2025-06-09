package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
import com.covid_stats.covid_stats.Services.GastronomyRevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GastronomyRevenueController {

    private final GastronomyRevenueService revenueService;

    public GastronomyRevenueController(GastronomyRevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping("/stats2")
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

    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping(value = "/stats2/export", produces = "text/csv")
    public void exportStats2Csv(HttpServletResponse response) throws IOException {
        Map<String, List<GastronomyRevenue>> dataMap =
                revenueService.loadGastronomyRevenueDataFromClasspath("data2.csv");
        List<GastronomyRevenue> data = dataMap.values().stream().findFirst().orElse(List.of());

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=stats2.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("year,revenue");
            for (GastronomyRevenue g : data) {
                writer.printf("%d,%.2f%n", g.getYear(), g.getRevenue());
            }
        }
    }
}