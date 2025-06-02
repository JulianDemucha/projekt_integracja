package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.GastronomyFacilitiesQuantity;
import com.covid_stats.covid_stats.Services.GastronomyFacilitiesQuantityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GastronomyFacilitiesQuantityContrroller {

    private final GastronomyFacilitiesQuantityService objectsService;

    public GastronomyFacilitiesQuantityContrroller(GastronomyFacilitiesQuantityService objectsService) {
        this.objectsService = objectsService;
    }
    @CrossOrigin(origins = "http://localhost:5173", methods = RequestMethod.GET)
    @GetMapping("/stats3")
    public ResponseEntity<?> getObjectsCount() {
        try {
            Map<String, List<GastronomyFacilitiesQuantity>> data =
                    objectsService.loadObjectsCountData("data3.csv");

            List<GastronomyFacilitiesQuantity> flatList = new ArrayList<>();
            for (List<GastronomyFacilitiesQuantity> listaPoRegionie : data.values()) {
                flatList.addAll(listaPoRegionie);
            }

            return ResponseEntity.ok(flatList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd wczytywania pliku CSV: " + e.getMessage());
        }
    }
}
