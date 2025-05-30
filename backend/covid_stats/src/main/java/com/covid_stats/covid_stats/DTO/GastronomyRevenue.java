package com.covid_stats.covid_stats.DTO;

import lombok.Getter;

public class GastronomyRevenue {
    @Getter
    int year;
    @Getter
    double revenue;

    public GastronomyRevenue(int year, double revenue) {
        this.year = year;
        this.revenue = revenue;
    }
}
