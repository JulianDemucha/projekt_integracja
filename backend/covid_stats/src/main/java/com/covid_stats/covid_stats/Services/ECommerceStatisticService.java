package com.covid_stats.covid_stats.Services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.covid_stats.covid_stats.DTO.ProcentPrzedsiebiorstw;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ECommerceStatisticService {
    private final List<ProcentPrzedsiebiorstw> stats = new ArrayList<>();

    @PostConstruct
    public void load() {
        Pattern pattern = Pattern.compile("ent;(.*?);;");  // Twój wzorzec regex

        try (InputStream is = getClass().getResourceAsStream("/data.csv")) {
            if (is == null) {
                throw new IllegalStateException("Nie znaleziono /data.csv na classpath");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String extracted = matcher.group(1);
                        String[] split = extracted.replace(",", ".").split(";");

                        for (int i = 0; i < split.length; i++) {
                            stats.add(new ProcentPrzedsiebiorstw(
                                    Double.parseDouble(split[i].trim()),
                                    2013 + i
                            ));
                        }
                        // jeśli chcesz wczytać tylko pierwszą pasującą linię, to break tutaj:
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Błąd wczytywania danych z data.csv", e);
        }
    }

    public List<ProcentPrzedsiebiorstw> getStats(int from, int to) {
        return stats.stream()
                .filter(s -> s.getRok() >= from && s.getRok() <= to)
                .toList();
    }
}
