package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Services.ECommerceStatisticService;
import com.covid_stats.covid_stats.DTO.ECommerceEnterprisesPercent;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ECommerceStatisticController {
    private final ECommerceStatisticService service;

    public ECommerceStatisticController(ECommerceStatisticService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping("/stats")
    public List<ECommerceEnterprisesPercent> stats(
            @RequestParam(name="from", defaultValue="2013") int from,
            @RequestParam(name="to",   defaultValue="2023") int to) {
        return service.getStats(from, to);
    }

    //nie uzywany filtering, ale w razie co jest
    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping(value = "/stats/export", produces = "text/csv")
    public void exportStatsCsv(
            @RequestParam(name="from", defaultValue="2013") int from,
            @RequestParam(name="to",   defaultValue="2023") int to,
            HttpServletResponse response) throws IOException {
        List<ECommerceEnterprisesPercent> list = service.getStats(from, to);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=stats.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("year,percent");
            for (ECommerceEnterprisesPercent e : list) {
                writer.printf("%d,%.2f%n", e.getYear(), e.getPercent());
            }
        }
    }
}