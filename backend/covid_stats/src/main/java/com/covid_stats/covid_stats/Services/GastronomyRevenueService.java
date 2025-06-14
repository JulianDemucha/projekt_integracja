package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.DTO.GastronomyRevenue;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GastronomyRevenueService {

    public List<GastronomyRevenue> loadGastronomyRevenueDataFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        List<GastronomyRevenue> allData = new ArrayList<>();

        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            // Wczytaj i podziel nagłówek
            String headerLine = reader.readLine();
            if (headerLine == null) return allData;
            String[] headers = splitCsvLine(headerLine);

            // Znajdź indeksy kolumn i lata
            List<Integer> years = new ArrayList<>();
            List<Integer> colIndexes = new ArrayList<>();
            for (int i = 0; i < headers.length; i++) {
                String h = headers[i].replaceAll("^\"|\"$", "");
                if (h.startsWith("ogółem")) {
                    String[] parts = h.split(";");
                    if (parts.length >= 2) {
                        try {
                            years.add(Integer.parseInt(parts[1]));
                            colIndexes.add(i);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            // Przetwarzaj każdą linię danych
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);
                for (int idx = 0; idx < colIndexes.size(); idx++) {
                    int col = colIndexes.get(idx);
                    if (col >= parts.length) continue;
                    String raw = parts[col].replaceAll("^\"|\"$", "").trim();
                    if (raw.isEmpty()) continue;
                    String normalized = raw.replace(',', '.');
                    try {
                        double rev = Double.parseDouble(normalized);
                        allData.add(new GastronomyRevenue(years.get(idx), rev));
                    } catch (NumberFormatException ignore) {}
                }
            }
        }

        // Sortowanie po roku
        return allData.stream()
                .sorted(Comparator.comparingInt(GastronomyRevenue::getYear))
                .collect(Collectors.toList());
    }

    public byte[] generateFilteredCsv(String resourcePath, int minYear, int maxYear) throws IOException {
        List<GastronomyRevenue> filtered = loadGastronomyRevenueDataFromClasspath(resourcePath)
                .stream()
                .filter(d -> d.getYear() >= minYear && d.getYear() <= maxYear)
                .collect(Collectors.toList());

        // Budowanie CSV w pamięci
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {

            writer.println("year,revenue");
            for (GastronomyRevenue g : filtered) {
                writer.printf("%d,%.2f%n", g.getYear(), g.getRevenue());
            }
            writer.flush();
            return baos.toByteArray();
        }
    }

    private String[] splitCsvLine(String line) {
        if (line == null) return new String[0];
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                sb.append(c);
            } else if (c == ';' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}
