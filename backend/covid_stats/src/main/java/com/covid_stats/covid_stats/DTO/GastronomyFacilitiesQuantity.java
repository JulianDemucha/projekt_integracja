package com.covid_stats.covid_stats.DTO;

import lombok.Getter;
import lombok.Setter;

public class GastronomyFacilitiesQuantity {
    @Getter
    @Setter
    private int year;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private int count;

    public GastronomyFacilitiesQuantity(int year, String type, int count) {
        this.year = year;
        this.type = type;
        this.count = count;
    }

}
