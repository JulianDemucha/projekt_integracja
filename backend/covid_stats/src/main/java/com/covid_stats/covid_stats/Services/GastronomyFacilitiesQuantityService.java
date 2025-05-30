package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.DTO.GastronomyFacilitiesQuantity;
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
public class GastronomyFacilitiesQuantityService {

    /**
     * Parsuje pojedynczą linię CSV, gdzie separatorem jest ';', a pola mogą być otoczone cudzysłowami '"'.
     * Przykład wiersza nagłówka:
     *   "\"Kod\";\"Nazwa\";\"restauracje;2005;[ob.]\";\"restauracje;2006;[ob.]\";..."
     * Metoda zwraca tablicę String[] z odsłoniętymi polami (bez otaczających cudzysłowów).
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // jeśli trafimy na ", to powinno oznaczać wejście/wyjście z obszaru cytowanego pola
                inQuotes = !inQuotes;
                // nie dodajemy " do current – chcemy usunąć zewnętrzne cudzysłowy
            } else if (c == ';' && !inQuotes) {
                // separator poza cytowaniem: koniec pola
                fields.add(current.toString());
                current.setLength(0);
            } else {
                // normalny znak lub średnik wewnątrz cytowanego pola
                current.append(c);
            }
        }
        // dodaj ostatnie pole
        fields.add(current.toString());

        // Na tym etapie każde pole może zawierać jeszcze pozostałe cudzysłowy wewnątrz (jeśli były escaped).
        // W naszym przypadku występują tylko zewnętrzne cudzysłowy, więc możemy je usunąć:
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
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            // 1) Wczytaj nagłówek
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return result;
            }

            // Parsujemy nagłówek z naszą metodą:
            String[] headersRaw = parseCsvLine(headerLine);
            int totalColumns = headersRaw.length;

            // Przygotuj listy: indeksy kolumn, typy („restauracje”, „bary” itd.), lata
            List<Integer> columnIndexes = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();
            List<Integer> columnYears = new ArrayList<>();

            // Zakładamy: kolumny 0 i 1 to Kod + Nazwa, a od 2 dalej mamy pola typu "typ;rok;[ob.]"
            for (int i = 2; i < totalColumns; i++) {
                String raw = headersRaw[i]; // np. raw = "restauracje;2005;[ob.]"
                // Rozbijamy maksymalnie na trzy części: typ, rok, i resztę po średniku:
                String[] parts = raw.split(";", 3);
                if (parts.length >= 2) {
                    String type = parts[0].trim();      // "restauracje"
                    String yearStr = parts[1].trim();   // "2005"
                    try {
                        int year = Integer.parseInt(yearStr);
                        columnIndexes.add(i);
                        columnTypes.add(type);
                        columnYears.add(year);
                    } catch (NumberFormatException ex) {
                        // jeśli nie uda się sparsować roku, pomijamy tę kolumnę
                    }
                }
            }

            // 2) Wczytuj kolejne wiersze z danymi
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parsujemy wiersz tak samo:
                String[] parts = parseCsvLine(line);
                if (parts.length < 2) {
                    continue;
                }

                // Kolumny: parts[0] = „Kod”, parts[1] = „Nazwa” np. "POLSKA"
                String rawName = parts[1].trim();
                // (nasza parseCsvLine już usunęła ewentualne zewnętrzne cudzysłowy)
                String nameKey = rawName;

                List<GastronomyFacilitiesQuantity> list = new ArrayList<>();
                // Dla każdej zidentyfikowanej kolumny z nagłówka weź wartość i stwórz obiekt
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
}
