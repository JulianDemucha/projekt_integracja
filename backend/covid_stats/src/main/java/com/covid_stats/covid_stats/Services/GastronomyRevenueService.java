package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GastronomyRevenueService {

    public Map<String, List<GastronomyRevenue>> loadGastronomyRevenueDataFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        Map<String, List<GastronomyRevenue>> result = new LinkedHashMap<>();

        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return result;
            }

            String[] headers = headerLine.split(";");
            List<Integer> columnIndexes = new ArrayList<>();
            List<Integer> years = new ArrayList<>();

            int headerPartsCount = headers.length;
            for (int i = 2; i + 1 < headerPartsCount; i += 3) {
                String maybeYearString = headers[i + 1];
                try {
                    int yr = Integer.parseInt(maybeYearString.trim());
                    columnIndexes.add(i);
                    years.add(yr);
                } catch (NumberFormatException ex) {
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", -1);
                if (parts.length < 2) {
                    continue;
                }
                String nazwa = parts[1];
                List<GastronomyRevenue> list = new ArrayList<>();

                for (int idx = 0; idx < columnIndexes.size(); idx++) {
                    int colIndex = columnIndexes.get(idx);
                    int year = years.get(idx);

                    if (colIndex >= parts.length) {
                        continue;
                    }

                    String rawValue = parts[colIndex].trim();
                    if (rawValue.isEmpty()) {
                        continue;
                    }

                    String normalized = rawValue.replace(",", ".");
                    try {
                        double revenue = Double.parseDouble(normalized);
                        list.add(new GastronomyRevenue(year, revenue));
                    } catch (NumberFormatException e) {
                    }
                }

                result.put(nazwa, list);
            }
        }

        return result;
    }
}
