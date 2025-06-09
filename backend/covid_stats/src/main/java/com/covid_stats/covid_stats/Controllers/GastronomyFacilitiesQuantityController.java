package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyFacilitiesQuantity;
import com.covid_stats.covid_stats.Services.GastronomyFacilitiesQuantityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GastronomyFacilitiesQuantityController {

    private final GastronomyFacilitiesQuantityService objectsService;

    public GastronomyFacilitiesQuantityController(GastronomyFacilitiesQuantityService objectsService) {
        this.objectsService = objectsService;
    }

    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping("/stats3")
    public ResponseEntity<List<GastronomyFacilitiesQuantity>> getObjectsCount() {
        try {
            // Wczytujemy i zwracamy całość
            Map<String, List<GastronomyFacilitiesQuantity>> data =
                    objectsService.loadObjectsCountData("data3.csv");

            List<GastronomyFacilitiesQuantity> flatList = new ArrayList<>();
            data.values().forEach(flatList::addAll);

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
    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping(value = "/stats3/export", produces = "text/csv")
    public void exportStats3Csv(
            @RequestParam int minYear,
            @RequestParam int maxYear,
            @RequestParam(name = "types") List<String> types,
            HttpServletResponse response
    ) throws IOException {
        // wczytanie danych
        Map<String, List<GastronomyFacilitiesQuantity>> data =
                objectsService.loadObjectsCountData("data3.csv");
        List<GastronomyFacilitiesQuantity> flatList = new ArrayList<>();
        data.values().forEach(flatList::addAll);

        // filtrowanie wg przyjetych parametrow
        List<GastronomyFacilitiesQuantity> filtered = flatList.stream()
                .filter(g -> g.getYear() >= minYear && g.getYear() <= maxYear)
                .filter(g -> types.contains(g.getType()))
                .collect(Collectors.toList());

        // tworzenie formatu do csv
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=stats3_filtered.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("year,type,count");
            for (GastronomyFacilitiesQuantity g : filtered) {
                writer.printf("%d,%s,%d%n", g.getYear(), g.getType(), g.getCount());
            }
        }
    }
}
