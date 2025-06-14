package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Services.ECommerceStatisticService;
import com.covid_stats.covid_stats.DTO.ECommerceEnterprisesPercent;
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
public class ECommerceStatisticController {
    private final ECommerceStatisticService service;

    public ECommerceStatisticController(ECommerceStatisticService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    public List<ECommerceEnterprisesPercent> stats(
            @RequestParam(name="from", defaultValue="2013") int from,
            @RequestParam(name="to",   defaultValue="2023") int to) {
        return service.getStats(from, to);
    }

    // nie używany filtering, ale w razie co jest
    @GetMapping(value = "/stats/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportStatsCsv(
            @RequestParam(name="from", defaultValue="2013") int from,
            @RequestParam(name="to",   defaultValue="2023") int to
    ) {
        try {
            byte[] csvBytes = service.generateFilteredCsv(from, to);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stats.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

