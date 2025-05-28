package com.covid_stats.covid_stats.DTO;

import lombok.Getter;

public class ECommerceEnterprisesPercent {
    @Getter
    double percent;
    @Getter
    int year;

    public ECommerceEnterprisesPercent(double procent, int year) {
        this.percent = procent;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Procent przedsiebiorstw umozliwiajacych zamawianie online w roku "
                + year + ": "+ percent +"%";
    }
}
