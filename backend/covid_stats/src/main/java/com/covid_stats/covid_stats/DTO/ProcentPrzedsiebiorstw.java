package com.covid_stats.covid_stats.DTO;

public class ProcentPrzedsiebiorstw {
    double procent;
    int rok;

    public ProcentPrzedsiebiorstw(double procent, int rok) {
        this.procent = procent;
        this.rok = rok;
    }

    public double getProcent() {
        return procent;
    }

    public int getRok() {
        return rok;
    }

    @Override
    public String toString() {
        return "Procent przedsiebiorstw umozliwiajacych zamawianie online w roku "
                + rok + ": "+procent+"%";
    }
}
