package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.DTO.GastronomyFacilitiesQuantity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GastronomyFacilitiesQuantityService {

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;

            } else if (c == ';' && !inQuotes) {

                fields.add(current.toString());
                current.setLength(0);
            } else {

                current.append(c);
            }
        }

        fields.add(current.toString());
        for (int i = 0; i < fields.size(); i++) {
            String f = fields.get(i).trim();
            if (f.length() >= 2 && f.startsWith("\"") && f.endsWith("\"")) {
                f = f.substring(1, f.length() - 1);
            }
            fields.set(i, f);
        }

        return fields.toArray(new String[0]);
    }

    public Map<String, List<GastronomyFacilitiesQuantity>> loadObjectsCountData(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Nie znaleziono pliku w classpath: " + resourcePath);
        }

        Map<String, List<GastronomyFacilitiesQuantity>> result = new LinkedHashMap<>();

        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return result;
            }

            String[] headersRaw = parseCsvLine(headerLine);
            int totalColumns = headersRaw.length;

            List<Integer> columnIndexes = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();
            List<Integer> columnYears = new ArrayList<>();

            for (int i = 2; i < totalColumns; i++) {
                String raw = headersRaw[i];
                String[] parts = raw.split(";", 3);
                if (parts.length >= 2) {
                    String type = parts[0].trim();
                    String yearStr = parts[1].trim();
                    try {
                        int year = Integer.parseInt(yearStr);
                        columnIndexes.add(i);
                        columnTypes.add(type);
                        columnYears.add(year);
                    } catch (NumberFormatException ex) {}
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = parseCsvLine(line);
                if (parts.length < 2) {
                    continue;
                }

                String rawName = parts[1].trim();
                String nameKey = rawName;

                List<GastronomyFacilitiesQuantity> list = new ArrayList<>();
                for (int idx = 0; idx < columnIndexes.size(); idx++) {
                    int colIndex = columnIndexes.get(idx);
                    String type = columnTypes.get(idx);
                    int year = columnYears.get(idx);

                    if (colIndex >= parts.length) {
                        continue;
                    }
                    String rawValue = parts[colIndex].trim();
                    try {
                        int count = Integer.parseInt(rawValue);
                        list.add(new GastronomyFacilitiesQuantity(year, type, count));
                    } catch (NumberFormatException e) {
                        // puste pola lub niepoprawne liczby pomijamy
                    }
                }

                result.put(nameKey, list);
            }
        }

        return result;
    }

    public List<GastronomyFacilitiesQuantity> loadFlatData(String resourcePath) throws IOException {
        Map<String, List<GastronomyFacilitiesQuantity>> data = loadObjectsCountData(resourcePath);
        return data.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public byte[] generateFilteredCsv(String resourcePath, int minYear, int maxYear, List<String> types) throws IOException {
        List<GastronomyFacilitiesQuantity> filtered = loadFlatData(resourcePath).stream()
                .filter(g -> g.getYear() >= minYear && g.getYear() <= maxYear)
                .filter(g -> types.contains(g.getType()))
                .collect(Collectors.toList());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {
            writer.println("year,type,count");
            for (GastronomyFacilitiesQuantity g : filtered) {
                writer.printf("%d,%s,%d%n", g.getYear(), g.getType(), g.getCount());
            }
            writer.flush();
            return baos.toByteArray();
        }
    }
}
