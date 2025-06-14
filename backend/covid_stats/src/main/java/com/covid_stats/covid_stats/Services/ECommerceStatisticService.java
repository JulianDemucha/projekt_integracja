package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.DTO.ECommerceEnterprisesPercent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ECommerceStatisticService {
    private final List<ECommerceEnterprisesPercent> stats = new ArrayList<>();

    @PostConstruct
    public void load() {
        Pattern pattern = Pattern.compile("ent;(.*?);;");

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
                            stats.add(new ECommerceEnterprisesPercent(
                                    Double.parseDouble(split[i].trim()),
                                    2013 + i
                            ));
                        }

                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Błąd wczytywania danych z data.csv", e);
        }
    }

    public List<ECommerceEnterprisesPercent> getStats(int from, int to) {
        return stats.stream()
                .filter(s -> s.getYear() >= from && s.getYear() <= to)
                .collect(Collectors.toList());
    }

    public byte[] generateFilteredCsv(int from, int to) throws IOException {
        List<ECommerceEnterprisesPercent> filtered = getStats(from, to);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {

            writer.println("year,percent");
            for (ECommerceEnterprisesPercent e : filtered) {
                writer.printf("%d,%.2f%n", e.getYear(), e.getPercent());
            }
            writer.flush();
            return baos.toByteArray();
        }
    }
}
