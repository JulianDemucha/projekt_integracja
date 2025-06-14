package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyFacilitiesQuantity;
import com.covid_stats.covid_stats.Services.GastronomyFacilitiesQuantityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class GastronomyFacilitiesQuantityController {

    private final GastronomyFacilitiesQuantityService objectsService;

    public GastronomyFacilitiesQuantityController(GastronomyFacilitiesQuantityService objectsService) {
        this.objectsService = objectsService;
    }

    @GetMapping("/stats3")
    public ResponseEntity<List<GastronomyFacilitiesQuantity>> getObjectsCount() {
        try {
            // Wczytujemy i zwracamy całość
            List<GastronomyFacilitiesQuantity> flatList =
                    objectsService.loadFlatData("data3.csv");
            return ResponseEntity.ok(flatList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(Collections.emptyList());
        }
    }

    /**
     * przyjmuje minyear, maxyear i typy obiektow
     * gastronomicznych z filtrowanego wykresu na stronie
     */
    @GetMapping(value = "/stats3/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportStats3Csv(
            @RequestParam int minYear,
            @RequestParam int maxYear,
            @RequestParam(name = "types") List<String> types
    ) {
        try {
            byte[] csvBytes =
                    objectsService.generateFilteredCsv("data3.csv", minYear, maxYear, types);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stats3_filtered.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
